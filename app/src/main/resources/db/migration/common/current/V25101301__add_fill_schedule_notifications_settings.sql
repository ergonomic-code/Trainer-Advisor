CREATE TABLE therapist_fill_schedule_notifications_settings
(
    id             UUID PRIMARY KEY REFERENCES therapists,
    enabled        BOOLEAN NOT NULL,
    day_of_week    VARCHAR NOT NULL,
    scheduled_time TIME    NOT NULL
);
