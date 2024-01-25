package pro.qyoga.tests.platform

import java.io.InputStream
import java.nio.file.Path


fun storeArtifact(
    name: Path,
    artifact: InputStream,
    dir: Path = Path.of(System.getProperty("user.dir"), "build", "reports", "tests", "artifacts")
) {
    val file = dir.resolve(name).toFile()
    file.parentFile.mkdirs()
    file.outputStream().use {
        artifact.copyTo(it)
    }
}