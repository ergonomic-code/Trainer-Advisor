-- password: "password"
INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('therapist@trainer-advisor.pro',
        '$2a$12$TbB4qOKdLj8lJ1Ml1SlZ.eG/mQGjQ4Ehrz20CjHovlEG2D6QBwLMe',
        '{"ROLE_THERAPIST"}',
        now(),
        1);

INSERT INTO therapists (id, first_name, last_name, created_at, version)
VALUES ((SELECT id FROM users WHERE email = 'therapist@trainer-advisor.pro'),
        'Елена', 'Маркова',
        now(),
        1);