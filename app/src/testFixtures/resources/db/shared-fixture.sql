TRUNCATE
    users, clients, therapists,
    exercises, exercise_steps,
    therapeutic_tasks,
    journal_entries, files, client_files,
    programs, program_exercises,
    appointments, appointment_types,
    survey_forms_settings,
    ical_calendars,
    therapist_google_accounts
    RESTART IDENTITY;

INSERT INTO users (id, email, password_hash, roles, created_at, version)
VALUES ('d43cf6b1-8b8c-4b45-b48c-b350dd99e497', 'therapist@qyoga.pro', 'password', '{"ROLE_THERAPIST"}', now(), 1);

INSERT INTO therapists(id, first_name, last_name, created_at, version)
VALUES ('d43cf6b1-8b8c-4b45-b48c-b350dd99e497', 'Елена (тест)', 'Маркова', now(), 1);

INSERT INTO users (id, email, password_hash, roles, created_at, version)
VALUES ('95cde197-9490-4d39-be46-764ba2107f79', 'admin@ta.pro', 'password', '{"ROLE_ADMIN"}', now(), 1);

INSERT INTO therapists(id, first_name, last_name, created_at, version)
VALUES ('95cde197-9490-4d39-be46-764ba2107f79', 'Админ (тест)', 'Адамов', now(), 1);
