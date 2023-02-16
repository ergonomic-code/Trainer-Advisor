CREATE TABLE exercise_types
(
    id   SERIAL       NOT NULL
        CONSTRAINT pk_exercise_types PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE exercises
(
    id               SERIAL       NOT NULL
        CONSTRAINT pk_exercises PRIMARY KEY,
    title            VARCHAR(128) NOT NULL,
    description      VARCHAR(255) NOT NULL,
    indications      VARCHAR(255) NOT NULL,
    contradictions   VARCHAR(255) NOT NULL,
    duration         VARCHAR(128) NOT NULL,
    exercise_type_id INTEGER      NOT NULL
        CONSTRAINT fk_exercises_exercise_type_ids REFERENCES exercise_types
            ON DELETE CASCADE,
    therapist_id     INTEGER      NOT NULL
        CONSTRAINT fk_exercises_therapist_ids REFERENCES therapists
            ON DELETE CASCADE
);

CREATE TABLE exercise_steps
(
    id          SERIAL       NOT NULL
        CONSTRAINT pk_exercise_steps PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    photo       VARCHAR(128),
    exercise_id INTEGER      NOT NULL
        CONSTRAINT fk_exercise_steps_exercises REFERENCES exercises
            ON DELETE CASCADE
);

INSERT INTO exercise_types
VALUES (1, 'Resistance Breathing'),
       (2, 'Balance'),
       (3, 'Stretching'),
       (4, 'Weight Bearing Exercise'),
       (5, 'Strength Training'),
       (6, 'CardioTraining');

CREATE TABLE programs
(
    id    SERIAL NOT NULL
        CONSTRAINT pk_programs PRIMARY KEY,
    title VARCHAR(255),
    date  VARCHAR(128)
);

CREATE TABLE therapeutic_purposes
(
    id      SERIAL NOT NULL
        CONSTRAINT pk_therapeutic_purposes PRIMARY KEY,
    purpose VARCHAR(255)
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


