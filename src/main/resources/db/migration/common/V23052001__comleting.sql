CREATE TABLE completing
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    completing_date  date,
    numeric_result   INT,
    text_result      VARCHAR,
    questionnaire_id BIGINT NOT NULL REFERENCES questionnaires (id),
    client_id        BIGINT NOT NULL REFERENCES clients (id),
    therapist_id     BIGINT NOT NULL REFERENCES therapists (id)
)