CREATE TABLE programs
(
    id                   BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

    title                VARCHAR                             NOT NULL,
    therapeutic_task_ref BIGINT REFERENCES therapeutic_tasks NOT NULL,
    owner_ref            BIGINT REFERENCES therapists        NOT NULL,

    created_at           TIMESTAMPTZ                         NOT NULL,
    modified_at          TIMESTAMPTZ,
    version              BIGINT                              NOT NULL
);

CREATE TABLE program_exercises
(
    program_id     BIGINT REFERENCES programs  NOT NULL,
    exercise_index SMALLINT                    NOT NULL,
    exercise_ref   BIGINT REFERENCES exercises NOT NULL,

    PRIMARY KEY (program_id, exercise_index)
);