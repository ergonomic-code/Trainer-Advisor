CREATE TABLE exercise_types
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
        CONSTRAINT pk_exercise_types PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE exercises
(
    id               SERIAL   NOT NULL
        CONSTRAINT pk_exercises PRIMARY KEY,
    title            VARCHAR  NOT NULL,
    description      TEXT     NOT NULL,
    indications      TEXT     NOT NULL,
    contradictions   TEXT     NOT NULL,
    duration         INTERVAL NOT NULL,
    exercise_type_id INTEGER  NOT NULL
        CONSTRAINT fk_exercises_exercise_type_ids REFERENCES exercise_types
            ON DELETE CASCADE,
    therapist_id     INTEGER  NOT NULL
        CONSTRAINT fk_exercises_therapist_ids REFERENCES therapists
            ON DELETE CASCADE
);

CREATE TABLE exercise_steps
(
    id          SERIAL  NOT NULL
        CONSTRAINT pk_exercise_steps PRIMARY KEY,
    description VARCHAR NOT NULL,
    photo       VARCHAR,
    exercise_id INTEGER NOT NULL
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
    id    SERIAL NOT NULL
        CONSTRAINT pk_programs PRIMARY KEY,
    title VARCHAR,
    date  TIMESTAMP
);

CREATE TABLE therapeutic_purposes
(
    id      SERIAL NOT NULL
        CONSTRAINT pk_therapeutic_purposes PRIMARY KEY,
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


