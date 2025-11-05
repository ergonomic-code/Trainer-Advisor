CREATE TABLE therapist_web_push_subscriptions
(
    id            UUID PRIMARY KEY,
    therapist_ref UUID                     NOT NULL REFERENCES therapists ON DELETE CASCADE ON UPDATE CASCADE,
    subscription  JSONB                    NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE          DEFAULT NULL,
    version       BIGINT                   NOT NULL DEFAULT 1,
    p256dh        VARCHAR(256)             NOT NULL GENERATED ALWAYS AS (subscription -> 'keys' ->> 'p256dh') STORED,
    UNIQUE (therapist_ref, p256dh)
);
