CREATE TABLE journal_entries
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client           BIGINT      NOT NULL REFERENCES clients,
    date             DATE        NOT NULL,
    therapeutic_task BIGINT REFERENCES therapeutic_tasks,
    entry_text       VARCHAR,
    created_at       TIMESTAMPTZ NOT NULL,
    last_modified_at TIMESTAMPTZ,
    version          BIGINT      NOT NULL,
    UNIQUE (client, date)
)