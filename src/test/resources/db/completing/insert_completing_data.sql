INSERT INTO clients
    (first_name, last_name, patronymic, birth_date, phone_number, diagnose, email, distribution_source)
VALUES
    ('first_name', 'last_name', 'patronymic', '2023-05-20', 'phone_number', 'diagnose', 'email', 'test'),
    ('first_name', 'last_name', 'patronymic', '2023-05-20', 'phone_number', 'diagnose', 'email', 'test');

insert into users
    (username, password_hash, roles)
values
    ('therapist2', '$2y$10$rfVqttKA1NL/C7BpPjZn7eeQ/QiI2z.iPzbt4TleECJrq8KSkSihi', '{"ROLE_THERAPIST"}');

INSERT into therapists
    (user_id, name)
VALUES
    ((SELECT id from users WHERE username = 'therapist'),
        'Екатерина Маркова');

INSERT INTO completing
    (completing_date, numeric_result, text_result, questionnaire_id, client_id, therapist_id)
VALUES
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 1),
    ('2023-06-22', 10, 'large_text_result_123456789011121314151617181920', 2, 1, 1),
    ('2023-07-22', 10, 'large_text_result_123456789011121314151617181920', 3, 1, 1),
    ('2023-08-22', 10, 'large_text_result_123456789011121314151617181920', 4, 1, 1),
    ('2023-09-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 1),
    ('2023-01-22', 10, 'large_text_result_123456789011121314151617181920', 6, 1, 1),
    ('2023-02-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 1),
    ('2023-03-22', 10, 'large_text_result_123456789011121314151617181920', 10, 1, 1),
    ('2023-04-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 9, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 9, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 9, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 9, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 9, 1, 1),
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 1)