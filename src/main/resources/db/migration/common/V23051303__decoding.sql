CREATE TABLE decoding
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lower_bound INT,
    upper_bound INT,
    result VARCHAR,
    questionnaire_id BIGINT NOT NULL REFERENCES questionnaires (id) ON DELETE CASCADE
)