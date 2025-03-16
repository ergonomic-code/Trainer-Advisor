CREATE TABLE survey_forms_settings
(
    therapist_ref      UUID PRIMARY KEY REFERENCES therapists ON UPDATE CASCADE ON DELETE CASCADE,
    yandex_admin_email VARCHAR,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_modified_at   TIMESTAMPTZ,
    version            BIGINT      NOT NULL DEFAULT 1
);