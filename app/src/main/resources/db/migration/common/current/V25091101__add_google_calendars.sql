CREATE TABLE therapist_google_accounts
(
    id            UUID PRIMARY KEY,
    owner_ref     UUID REFERENCES therapists NOT NULL,
    email         varchar                    NOT NULL,
    refresh_token varchar NOT NULL,

    UNIQUE (owner_ref, email)
);

CREATE TABLE therapist_google_calendar_settings
(
    id                 UUID PRIMARY KEY,
    owner_ref          UUID REFERENCES therapists                NOT NULL,
    google_account_ref UUID REFERENCES therapist_google_accounts NOT NULL,
    calendar_id        varchar                                   NOT NULL,
    should_be_shown    BOOLEAN                                   NOT NULL,

    UNIQUE (owner_ref, google_account_ref, calendar_id)
);