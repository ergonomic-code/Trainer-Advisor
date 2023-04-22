CREATE TABLE clients
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR,
    last_name  VARCHAR
)