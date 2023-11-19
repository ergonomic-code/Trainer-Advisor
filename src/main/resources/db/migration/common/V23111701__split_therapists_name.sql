ALTER TABLE therapists
    ADD COLUMN first_name varchar NOT NULL DEFAULT '',
    ADD COLUMN last_name  varchar NOT NULL DEFAULT '';

-- noinspection SqlWithoutWhere
UPDATE therapists
SET first_name = split_part(name, ' ', 1),
    last_name  = split_part(name, ' ', 2);

ALTER TABLE therapists
    DROP COLUMN "name";