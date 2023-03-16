CREATE TABLE questions
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT,
    question_type VARCHAR,
    questionnaire_id BIGINT NOT NULL,
    image_id BIGINT,
    FOREIGN KEY (questionnaire_id) REFERENCES questionnaires (id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE SET NULL
)