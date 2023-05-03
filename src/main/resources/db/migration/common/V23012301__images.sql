CREATE TABLE images
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR,
    media_type VARCHAR,
    size       BIGINT,
    data       bytea
)