ALTER TABLE appointments
    ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE appointments
    DROP CONSTRAINT appointments_pkey CASCADE;
ALTER TABLE appointments
    ADD PRIMARY KEY (new_id);
ALTER TABLE appointments
    DROP COLUMN id;
ALTER TABLE appointments
    RENAME COLUMN new_id TO id;
