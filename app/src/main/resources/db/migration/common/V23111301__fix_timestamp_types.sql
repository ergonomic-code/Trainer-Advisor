SET timezone = 'UTC';

ALTER TABLE users
    ALTER
        created_at TYPE TIMESTAMPTZ,
    ALTER
        modified_at TYPE TIMESTAMPTZ;

ALTER TABLE therapists
    ALTER
        created_at TYPE TIMESTAMPTZ,
    ALTER
        modified_at TYPE TIMESTAMPTZ;

ALTER TABLE clients
    ALTER
        created_at TYPE TIMESTAMPTZ,
    ALTER
        modified_at TYPE TIMESTAMPTZ;

ALTER TABLE images
    ALTER
        created_at TYPE TIMESTAMPTZ,
    ALTER
        modified_at TYPE TIMESTAMPTZ;

ALTER TABLE exercises
    ALTER
        created_at TYPE TIMESTAMPTZ,
    ALTER
        modified_at TYPE TIMESTAMPTZ;
