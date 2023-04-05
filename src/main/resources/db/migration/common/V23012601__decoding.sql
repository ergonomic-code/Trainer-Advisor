CREATE TABLE decoding
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lower_bound INT,
    upper_bound INT,
    result VARCHAR,
    question_id BIGINT NOT NULL REFERENCES questions (id) ON DELETE CASCADE
)