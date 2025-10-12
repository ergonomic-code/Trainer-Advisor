CREATE TABLE therapist_web_push_subscriptions
(
    therapist_ref   UUID                     NOT NULL,
    p256dh          VARCHAR(256)             NOT NULL,
    auth            VARCHAR(256)             NOT NULL,
    endpoint        VARCHAR(4096)            NOT NULL,
    expiration_time TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE          DEFAULT NULL,
    version         BIGINT                   NOT NULL DEFAULT 1,
    PRIMARY KEY (therapist_ref, p256dh)
);