INSERT INTO questionnaires (title) VALUES ('title');

INSERT INTO questions (title, question_type, questionnaire_id, image_id)
VALUES ('title1', 'SINGLE', (SELECT id FROM questionnaires WHERE title = 'title'), null)
