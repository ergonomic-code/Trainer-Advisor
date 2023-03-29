create table images
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR,
    media_type VARCHAR NOT NULL,
    size       BIGINT,
    data       BYTEA
);
