INSERT INTO clients
    (first_name, last_name, patronymic, birth_date, phone_number, diagnose, email, distribution_source)
VALUES
    ('first_name', 'last_name', 'patronymic', '2023-05-20', 'phone_number', 'diagnose', 'email', 'test'),
    ('first_name2', 'last_name2', 'patronymic2', '2023-05-20', 'phone_number2', 'diagnose2', 'email2', 'test2');

INSERT INTO completing
    (completing_date, numeric_result, text_result, questionnaire_id, client_id, therapist_id)
VALUES
    ('2023-05-1', 10, 'large_text_result_123456789011121314151617181920', 1, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-2', 10, 'text_result', 2, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-3', 10, 'large_text_result_123456789011121314151617181920', 3, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-4', 10, 'result', 4, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-5', 10, 'large_text_result_123456789011121314151617181920', 5, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-6', 10, 'large_text_result_123456789011121314151617181920', 2, 2,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-7', 10, 'large_text_result_123456789011121314151617181920', 1, 1,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-8', 10, 'large_text_result_123456789011121314151617181920', 2, 2,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-9', 10, 'large_text_result_123456789011121314151617181920', 6, 1,(SELECT id from therapists WHERE name = 'Тестовый Терапевт')),
    ('2023-05-10', 10, 'large_text_result_123456789011121314151617181920', 1, 2,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-11', 10, 'large_text_result_123456789011121314151617181920', 2, 1,(SELECT id from therapists WHERE name = 'Тестовый Терапевт')),
    ('2023-05-12', 10, 'large_text_result_123456789011121314151617181920', 7, 2,(SELECT id from therapists WHERE name = 'Екатерина Маркова')),
    ('2023-05-13', 10, 'large_text_result_123456789011121314151617181920', 2, 2,(SELECT id from therapists WHERE name = 'Екатерина Маркова'))