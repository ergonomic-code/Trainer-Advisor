INSERT INTO questionnaires (title) VALUES ('title');

INSERT INTO questions (title, question_type, questionnaire_id, image_id)
VALUES ('questionSINGLE', 'SINGLE', (SELECT id FROM questionnaires WHERE title = 'title'), null),
       ('questionSEVERAL', 'SEVERAL', (SELECT id FROM questionnaires WHERE title = 'title'), null),
       ('questionRANGE', 'RANGE', (SELECT id FROM questionnaires WHERE title = 'title'), null),
       ('questionTEXT', 'TEXT', (SELECT id FROM questionnaires WHERE title = 'title'), null);

INSERT INTO answers (title, lower_bound, lower_bound_text, upper_bound, upper_bound_text, score, question_id, image_id)
VALUES ('questionSINGLEAnswer', null, null, null, null, 10, (SELECT id FROM questions WHERE title = 'questionSINGLE'), null),
       ('questionSEVERALAnswer', null, null, null, null, 8, (SELECT id FROM questions WHERE title = 'questionSEVERAL'), null),
       ('questionRANGEAnswer', 2, 'lower_bound_text', 7, 'upper_bound_text', 10, (SELECT id FROM questions WHERE title = 'questionRANGE'), null),
       ('questionTEXTAnswer', null, null, null, null, 10, (SELECT id FROM questions WHERE title = 'questionTEXT'), null);

INSERT INTO decoding (lower_bound, upper_bound, result, questionnaire_id)
VALUES (10, 20, 'test1', (SELECT id FROM questionnaires WHERE title = 'title')),
       (20, 30, 'test2', (SELECT id FROM questionnaires WHERE title = 'title')),
       (30, 40, 'test3', (SELECT id FROM questionnaires WHERE title = 'title'))
