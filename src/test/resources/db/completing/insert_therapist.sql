insert into users (username, password_hash, roles)
values ('therapist', '$2a$12$wAIeyLso8yKIlIlv62EeL.R1co2DmJdb5DitjmdP.qYZflJNMP.ua', '{"ROLE_THERAPIST"}');

INSERT into therapists (id, name)
VALUES ((SELECT id from users WHERE username = 'therapist'),
        'Екатерина Маркова');