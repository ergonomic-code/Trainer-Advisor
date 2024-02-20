ALTER TABLE clients
    RENAME COLUMN patronymic TO middle_name;

ALTER TABLE clients
    ADD COLUMN address      VARCHAR,
    ADD COLUMN complaints   VARCHAR NOT NULL                       DEFAULT '',
    ADD COLUMN therapist_id BIGINT  NOT NULL REFERENCES therapists DEFAULT 2;

ALTER TABLE clients
    ALTER COLUMN middle_name DROP NOT NULL,
    DROP CONSTRAINT clients_email_key;

ALTER TABLE clients
    ALTER COLUMN complaints DROP DEFAULT,
    ALTER COLUMN therapist_id DROP DEFAULT;