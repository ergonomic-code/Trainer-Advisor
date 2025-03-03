CREATE TABLE client_files
(
    id                BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

    client_ref        BIGINT REFERENCES clients NOT NULL,
    file_ref          BIGINT REFERENCES files   NOT NULL,
    journal_entry_ref BIGINT REFERENCES journal_entries,

    created_at        TIMESTAMPTZ               NOT NULL,
    modified_at       TIMESTAMPTZ,
    version           BIGINT                    NOT NULL
)