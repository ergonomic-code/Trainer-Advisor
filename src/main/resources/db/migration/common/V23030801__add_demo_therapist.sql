-- password: diem-Synergy5
insert into users (username, password_hash, roles)
values ('therapist', '$2a$12$wAIeyLso8yKIlIlv62EeL.R1co2DmJdb5DitjmdP.qYZflJNMP.ua', '{"ROLE_THERAPIST"}');
-- password: test
insert into users (username, password_hash, roles)
values ('therapist2', '$2y$10$rfVqttKA1NL/C7BpPjZn7eeQ/QiI2z.iPzbt4TleECJrq8KSkSihi', '{"ROLE_THERAPIST"}');

INSERT into therapists (user_id, name)
VALUES ((SELECT id from users WHERE username = 'therapist'),
        'Екатерина Маркова');