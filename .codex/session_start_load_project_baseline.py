#!/usr/bin/env python3

import argparse
from functools import lru_cache
from pathlib import Path
import subprocess
import sys

import yaml


KNOWN_MODULES = {
    "core",
    "ergonomic-architecture",
    "ergonomic-testing",
    "tdd",
    "database",
    "http-api",
    "kotlin",
    "spring",
    "junit",
    "kotest",
}
DEFAULT_FRAMEWORK_CHECKOUT_ROOT = ".agents/ergo"


@lru_cache(maxsize=1)
def repo_root() -> Path:
    return Path(
        subprocess.check_output(
            ["git", "rev-parse", "--show-toplevel"],
            text=True,
        ).strip()
    )


def resolve_agents_md_path(agents_md_path: str | None) -> Path:
    if not agents_md_path:
        return repo_root() / "AGENTS.md"

    path = Path(agents_md_path)
    if not path.is_absolute():
        path = repo_root() / path
    return path


def installed_framework_checkout_root(agents_md_path_arg: str | None) -> Path:
    agents_md_path = resolve_agents_md_path(agents_md_path_arg)
    if not agents_md_path.is_file():
        return Path(DEFAULT_FRAMEWORK_CHECKOUT_ROOT)

    for line in agents_md_path.read_text(encoding="utf-8").splitlines():
        prefix = "Framework checkout root: "
        if line.startswith(prefix):
            value = line[len(prefix):].strip().rstrip(".")
            return Path(value.strip("`"))

    return Path(DEFAULT_FRAMEWORK_CHECKOUT_ROOT)


def framework_src_root(agents_md_path_arg: str | None) -> Path:
    return repo_root() / installed_framework_checkout_root(agents_md_path_arg) / "src"


def normalize_repo_relative_path(path: Path) -> str:
    try:
        return path.resolve().relative_to(repo_root().resolve()).as_posix()
    except ValueError:
        return path.as_posix()


def load_framework_config(framework_config_path: str | None) -> tuple[set[str], str]:
    if not framework_config_path:
        return {"core"}, ""

    config_path = Path(framework_config_path)
    if not config_path.is_absolute():
        config_path = repo_root() / config_path

    if not config_path.is_file():
        raise RuntimeError(
            f"framework config file is missing: {normalize_repo_relative_path(config_path)}"
        )

    yaml_text = config_path.read_text(encoding="utf-8").rstrip("\n")
    try:
        config = yaml.safe_load(yaml_text) if yaml_text.strip() else {}
    except yaml.YAMLError as error:
        raise RuntimeError(f"invalid framework config YAML: {error}") from error

    if not isinstance(config, dict):
        raise RuntimeError("framework config must be a YAML mapping")
    if not all(isinstance(key, str) for key in config):
        raise RuntimeError("framework config keys must be strings")

    unknown_keys = set(config) - {"artifact_language", "modules"}
    if unknown_keys:
        raise RuntimeError(
            "framework config contains unknown keys: " + ", ".join(sorted(unknown_keys))
        )

    artifact_language = config.get("artifact_language")
    if artifact_language is not None and (
        not isinstance(artifact_language, str) or not artifact_language.strip()
    ):
        raise RuntimeError("framework config artifact_language must be a non-empty string")

    modules_value = config.get("modules")
    if modules_value is None:
        modules = {"core"}
    else:
        if not isinstance(modules_value, list) or not all(
            isinstance(module, str) and module for module in modules_value
        ):
            raise RuntimeError("framework config modules must be a list of module names")
        if len(modules_value) != len(set(modules_value)):
            raise RuntimeError("framework config modules must not contain duplicates")
        modules = set(modules_value)
        unknown_modules = modules - KNOWN_MODULES
        if unknown_modules:
            raise RuntimeError(
                "framework config contains unknown modules: "
                + ", ".join(sorted(unknown_modules))
            )
        if "core" not in modules:
            raise RuntimeError("framework config modules must include core")

    rel = normalize_repo_relative_path(config_path)
    rendered = (
        "# Framework config\n\n"
        f"`{rel}`\n\n"
        "```yaml\n"
        f"{yaml_text}\n"
        "```\n"
    )
    return modules, rendered


def read_required_file(path: Path) -> str:
    if not path.is_file():
        raise RuntimeError(
            f"mandatory framework context file is missing: "
            f"{normalize_repo_relative_path(path)}"
        )
    text = path.read_text(encoding="utf-8")
    if not text.strip():
        raise RuntimeError(
            f"mandatory framework context file is empty: "
            f"{normalize_repo_relative_path(path)}"
        )
    return text.rstrip("\n")


def load_index_metadata(path: Path) -> dict[str, object]:
    text = read_required_file(path)
    lines = text.splitlines()
    if not lines or lines[0] != "---":
        raise RuntimeError(f"context index has no YAML front matter: {path.name}")
    try:
        end = lines.index("---", 1)
        metadata = yaml.safe_load("\n".join(lines[1:end]))
    except (ValueError, yaml.YAMLError) as error:
        raise RuntimeError(f"invalid context index metadata in {path.name}: {error}") from error
    if not isinstance(metadata, dict):
        raise RuntimeError(f"context index metadata must be a mapping: {path.name}")
    if set(metadata) != {"requires_modules", "applies_when", "routing_order"}:
        raise RuntimeError(f"context index has unsupported metadata keys: {path.name}")

    required = metadata.get("requires_modules")
    applicability = metadata.get("applies_when")
    order = metadata.get("routing_order")
    if not isinstance(required, list) or not required or not all(
        isinstance(module, str) and module in KNOWN_MODULES for module in required
    ):
        raise RuntimeError(f"invalid requires_modules in context index: {path.name}")
    if not isinstance(applicability, str) or not applicability.strip():
        raise RuntimeError(f"invalid applies_when in context index: {path.name}")
    if not isinstance(order, int) or isinstance(order, bool):
        raise RuntimeError(f"invalid routing_order in context index: {path.name}")
    return metadata


def render_filtered_routes(context_root: Path, modules: set[str]) -> str:
    routes = []
    orders = set()
    for path in context_root.glob("*.md"):
        if path.name == "index.md":
            continue
        metadata = load_index_metadata(path)
        order = metadata["routing_order"]
        if order in orders:
            raise RuntimeError(f"duplicate context index routing_order: {order}")
        orders.add(order)
        required = set(metadata["requires_modules"])
        if required <= modules:
            routes.append(
                (order, normalize_repo_relative_path(path), metadata["applies_when"])
            )

    routes.sort()
    if not routes:
        raise RuntimeError("no context routes are enabled for configured modules")
    lines = ["# Разрешённые тематические маршруты", ""]
    lines.extend(
        f"{number}. `{path}` — {applicability}"
        for number, (_order, path, applicability) in enumerate(routes, start=1)
    )
    return "\n".join(lines)


def load_files(
    framework_config_path: str | None,
    agents_md_path: str | None,
) -> str:
    root = framework_src_root(agents_md_path)
    baseline = read_required_file(root / "project-baseline.md")
    protocol = read_required_file(root / "context" / "index.md")
    modules, config_chunk = load_framework_config(framework_config_path)
    routes = render_filtered_routes(root / "context", modules)
    chunks = [baseline, f"{protocol}\n\n{routes}"]

    config_chunk = config_chunk.rstrip("\n")
    if config_chunk:
        chunks.append(config_chunk)

    return "\n\n".join(chunks) + ("\n" if chunks else "")


def main() -> int:
    parser = argparse.ArgumentParser(add_help=False)
    parser.add_argument("--agents-md-path")
    parser.add_argument("--framework-config-path")
    args, _unknown = parser.parse_known_args()

    try:
        text = load_files(args.framework_config_path, args.agents_md_path)
    except RuntimeError as error:
        print(f"SessionStart framework context error: {error}", file=sys.stderr)
        return 1
    if text:
        sys.stdout.write(text)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
