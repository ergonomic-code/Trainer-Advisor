CREATE TABLE answers
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT,
    lower_bound INT,
    lower_bound_text VARCHAR,
    upper_bound INT,
    upper_bound_text VARCHAR,
    score INT,
    question_id BIGINT,
    image_id BIGINT,
    FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE SET NULL
)