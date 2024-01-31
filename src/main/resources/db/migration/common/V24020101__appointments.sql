CREATE TABLE appointment_types
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

    owner_ref   BIGINT REFERENCES therapists NOT NULL,
    name        varchar                      NOT NULL,

    created_at  TIMESTAMPTZ                  NOT NULL,
    modified_at TIMESTAMPTZ,
    version     BIGINT                       NOT NULL
);

CREATE TABLE appointments
(
    id                   BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

    therapist_ref        BIGINT REFERENCES therapists        NOT NULL,
    client_ref           BIGINT REFERENCES clients           NOT NULL,
    type_ref             BIGINT REFERENCES appointment_types NOT NULL,
    therapeutic_task_ref BIGINT REFERENCES therapeutic_tasks,

    date_time            TIMESTAMPTZ                         NOT NULL,
    time_zone            varchar                             NOT NULL,

    place                varchar,

    cost                 NUMERIC(6, 0) CHECK ( cost >= 0 ),
    payed                BOOL                                NOT NULL,

    comment              text,

    created_at           TIMESTAMPTZ                         NOT NULL,
    modified_at          TIMESTAMPTZ,
    version              BIGINT                              NOT NULL
)