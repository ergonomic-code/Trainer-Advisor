CREATE TABLE users
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email         VARCHAR UNIQUE NOT NULL,
    password_hash VARCHAR        NOT NULL,
    roles         VARCHAR[]      NOT NULL,
    created_at    TIMESTAMP      NOT NULL,
    modified_at   TIMESTAMP,
    version       BIGINT         NOT NULL
);

INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('admin@qyoga.pro', '$2a$12$wAIeyLso8yKIlIlv62EeL.R1co2DmJdb5DitjmdP.qYZflJNMP.ua', '{"ROLE_ADMIN"}', now(), 1);

CREATE TABLE therapists
(
    id          BIGINT REFERENCES users PRIMARY KEY,
    name        VARCHAR,
    created_at  TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    version     BIGINT    NOT NULL
);

create table clients
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name          VARCHAR     NOT NULL,
    last_name           VARCHAR     NOT NULL,
    patronymic          VARCHAR     NOT NULL,
    birth_date          DATE        NOT NULL,
    phone_number        VARCHAR(18) NOT NULL UNIQUE,
    email               VARCHAR UNIQUE,
    distribution_source VARCHAR,
    created_at          TIMESTAMP   NOT NULL,
    modified_at         TIMESTAMP,
    version             BIGINT      NOT NULL
);

CREATE TABLE images
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR   NOT NULL,
    media_type  VARCHAR   NOT NULL,
    size        BIGINT    NOT NULL,
    data        bytea     NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    version     BIGINT    NOT NULL
);

CREATE TABLE exercises
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title         VARCHAR   NOT NULL,
    description   TEXT      NOT NULL,
    duration      INTERVAL  NOT NULL,
    exercise_type VARCHAR   NOT NULL,
    therapist_id  BIGINT    NOT NULL REFERENCES therapists,
    created_at    TIMESTAMP NOT NULL,
    modified_at   TIMESTAMP,
    version       BIGINT    NOT NULL
);

CREATE TABLE exercise_steps
(
    exercise_id BIGINT  NOT NULL
        CONSTRAINT fk_exercise_steps_exercises REFERENCES exercises
            ON DELETE CASCADE,
    step_index  SMALLINT,
    description VARCHAR NOT NULL,
    image_id    BIGINT,

    PRIMARY KEY (exercise_id, step_index)
);
