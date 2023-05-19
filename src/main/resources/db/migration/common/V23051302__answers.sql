CREATE TABLE answers
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT,
    lower_bound INT,
    lower_bound_text VARCHAR,
    upper_bound INT,
    upper_bound_text VARCHAR,
    score INT,
    question_id BIGINT NOT NULL REFERENCES questions (id) ON DELETE CASCADE,
    image_id BIGINT REFERENCES images (id) ON DELETE SET NULL
)