TRUNCATE users, clients, therapists, exercises, exercise_steps, therapeutic_tasks, journal_entries RESTART IDENTITY;

INSERT INTO users (email, password_hash, roles, created_at, version)
VALUES ('therapist@qyoga.pro', 'password', '{"ROLE_THERAPIST"}', now(), 1);

INSERT INTO therapists(id, first_name, last_name, created_at, version)
VALUES (1, 'Елена (тест)', 'Маркова ', now(), 1);