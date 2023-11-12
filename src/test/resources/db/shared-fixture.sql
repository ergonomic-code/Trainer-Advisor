TRUNCATE users, clients, therapists, exercises, exercise_steps RESTART IDENTITY;

INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('therapist@qyoga.pro', 'password', '{"ROLE_THERAPIST"}', now(), 1);

INSERT INTO therapists(id, name, created_at, version)
VALUES (1, 'Елена Маркова (тест)', now(), 1);