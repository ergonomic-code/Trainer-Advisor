INSERT INTO clients
    (first_name, last_name, patronymic, birth_date, phone_number, diagnose, email, distribution_source)
VALUES
    ('first_name', 'last_name', 'patronymic', '2023-05-20', 'phone_number', 'diagnose', 'email', 'test');

INSERT INTO completing
    (completing_date, numeric_result, text_result, questionnaire_id, client_id, therapist_id)
VALUES
    ('2023-05-22', 10, 'large_text_result_123456789011121314151617181920', 1, 1, 2)