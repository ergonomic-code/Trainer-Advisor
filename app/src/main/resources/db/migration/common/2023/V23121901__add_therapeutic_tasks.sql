CREATE TABLE therapeutic_tasks
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name             VARCHAR     NOT NULL,
    owner            BIGINT      NOT NULL REFERENCES therapists,
    created_at       TIMESTAMPTZ NOT NULL,
    last_modified_at TIMESTAMPTZ,
    version          BIGINT      NOT NULL,
    UNIQUE (owner, name)
);