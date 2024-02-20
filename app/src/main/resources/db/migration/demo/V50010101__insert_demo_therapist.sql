CREATE OR REPLACE FUNCTION usr(user_email varchar) RETURNS BIGINT
AS
'SELECT id
 FROM users
 WHERE email = user_email'
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT;



-- password: "password"
INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('therapist@trainer-advisor.pro',
        '$2a$12$TbB4qOKdLj8lJ1Ml1SlZ.eG/mQGjQ4Ehrz20CjHovlEG2D6QBwLMe',
        '{"ROLE_THERAPIST"}',
        now(),
        1);

INSERT INTO therapists (id, first_name, last_name, created_at, version)
VALUES (usr('therapist@trainer-advisor.pro'),
        'Елена', 'Маркова',
        now(),
        1);

-- password: "password"
INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('admin@ta.pro',
        '$2a$12$TbB4qOKdLj8lJ1Ml1SlZ.eG/mQGjQ4Ehrz20CjHovlEG2D6QBwLMe',
        '{"ROLE_ADMIN"}',
        now(),
        1);

-- password: "password"
INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('empty@trainer-advisor.pro',
        '$2a$12$TbB4qOKdLj8lJ1Ml1SlZ.eG/mQGjQ4Ehrz20CjHovlEG2D6QBwLMe',
        '{"ROLE_THERAPIST"}',
        now(),
        1);

INSERT INTO therapists (id, first_name, last_name, created_at, version)
VALUES (usr('empty@trainer-advisor.pro'),
        'Абсент', 'Абсентов',
        now(),
        1);
