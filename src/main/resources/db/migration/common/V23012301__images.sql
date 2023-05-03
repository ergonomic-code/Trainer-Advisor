CREATE TABLE images
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR NOT NULL,
    media_type VARCHAR NOT NULL,
    size       BIGINT NOT NULL,
    data       bytea NOT NULL
)