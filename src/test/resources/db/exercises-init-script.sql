TRUNCATE exercises, programs, exercise_steps, therapeutic_purposes, purpose_programs, exercise_purposes, appointments RESTART IDENTITY;

DELETE
FROM qyoga.public.therapists
WHERE id <> (SELECT id FROM qyoga.public.therapists where qyoga.public.therapists.name = 'therapist');
