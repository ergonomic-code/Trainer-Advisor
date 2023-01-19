CREATE TABLE users
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      VARCHAR UNIQUE,
    password_hash VARCHAR,
    roles         VARCHAR[]
);

-- password: diem-Synergy5
INSERT INTO users (username, password_hash, roles)
VALUES ('admin', '$2a$12$wAIeyLso8yKIlIlv62EeL.R1co2DmJdb5DitjmdP.qYZflJNMP.ua', '{"ROLE_ADMIN"}');


CREATE TABLE therapists
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users,
    name    VARCHAR
);

