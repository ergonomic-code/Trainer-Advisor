CREATE TABLE therapist_google_accounts
(
    id            UUID PRIMARY KEY,
    owner_ref     UUID REFERENCES therapists NOT NULL,
    email         varchar                    NOT NULL,
    refresh_token varchar NOT NULL,

    UNIQUE (owner_ref, email)
)