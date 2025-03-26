CREATE TABLE ical_calendars
(
    id               UUID PRIMARY KEY,
    owner_ref        UUID        NOT NULL REFERENCES therapists ON UPDATE CASCADE ON DELETE CASCADE,
    name             VARCHAR     NOT NULL,
    ics_url          VARCHAR     NOT NULL,
    ics_file         TEXT        NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_modified_at TIMESTAMPTZ,
    version          BIGINT      NOT NULL,
    UNIQUE (owner_ref, name)
)