CREATE TABLE exercise_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE exercises
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title            VARCHAR  NOT NULL,
    description      TEXT     NOT NULL,
    indications      TEXT     NOT NULL,
    contradictions   TEXT     NOT NULL,
    duration         INTERVAL NOT NULL,
    exercise_type_id BIGINT NOT NULL
        CONSTRAINT fk_exercises_exercise_type_ids REFERENCES exercise_types
            ON DELETE CASCADE,
    therapist_id     BIGINT NOT NULL
        CONSTRAINT fk_exercises_therapist_ids REFERENCES therapists
            ON DELETE CASCADE
);

CREATE TABLE exercise_steps
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR NOT NULL,
    photo       BYTEA,
    exercise_id BIGINT  NOT NULL
        CONSTRAINT fk_exercise_steps_exercises REFERENCES exercises
            ON DELETE CASCADE
);

INSERT INTO exercise_types(name)
VALUES ('WarmUp'),
       ('Mobilization'),
       ('Strengthening'),
       ('Stretching'),
       ('Relaxation'),
       ('Traction');

CREATE TABLE programs
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR,
    date  TIMESTAMP
);

CREATE TABLE therapeutic_purposes
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    purpose VARCHAR
);

create table purpose_programs
(
    purpose_id BIGINT REFERENCES therapeutic_purposes (id) ON UPDATE CASCADE ON DELETE CASCADE,
    program_id BIGINT REFERENCES programs (id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table exercise_purposes
(
    exercise_id BIGINT REFERENCES exercises (id) ON UPDATE CASCADE ON DELETE CASCADE,
    purpose_id BIGINT REFERENCES therapeutic_purposes (id) ON UPDATE CASCADE ON DELETE CASCADE
);
