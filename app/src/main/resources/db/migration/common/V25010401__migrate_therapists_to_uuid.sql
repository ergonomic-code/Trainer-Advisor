ALTER TABLE users
    ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE users
    DROP CONSTRAINT users_pkey CASCADE;
ALTER TABLE users
    ADD PRIMARY KEY (new_id);

ALTER TABLE therapists
    ADD COLUMN new_id UUID;
UPDATE therapists t
SET new_id = u.new_id
FROM users u
WHERE t.id = u.id;
ALTER TABLE therapists
    DROP CONSTRAINT therapists_pkey CASCADE;
ALTER TABLE therapists
    ADD PRIMARY KEY (new_id);

ALTER TABLE clients
    ADD COLUMN new_therapist_id UUID;
UPDATE clients c
SET new_therapist_id = t.new_id
FROM therapists t
WHERE t.id = c.therapist_id;

ALTER TABLE therapeutic_tasks
    ADD COLUMN owner_ref UUID;
UPDATE therapeutic_tasks tt
SET owner_ref = t.new_id
FROM therapists t
WHERE t.id = tt.owner;

ALTER TABLE appointment_types
    ADD COLUMN new_owner_ref UUID;
UPDATE appointment_types at
SET new_owner_ref = t.new_id
FROM therapists t
WHERE t.id = at.owner_ref;

ALTER TABLE programs
    ADD COLUMN new_owner_ref UUID;
UPDATE programs p
SET new_owner_ref = t.new_id
FROM therapists t
WHERE t.id = p.owner_ref;

ALTER TABLE exercises
    ADD COLUMN owner_ref UUID;
UPDATE exercises e
SET owner_ref = t.new_id
FROM therapists t
WHERE t.id = e.therapist_id;

ALTER TABLE appointments
    ADD COLUMN new_therapist_ref UUID;
UPDATE appointments a
SET new_therapist_ref = t.new_id
FROM therapists t
WHERE t.id = a.therapist_ref;

ALTER TABLE clients
    DROP COLUMN therapist_id;
ALTER TABLE clients
    RENAME COLUMN new_therapist_id TO therapist_ref;
ALTER TABLE clients
    ALTER COLUMN therapist_ref SET NOT NULL;
ALTER TABLE clients
    ADD CONSTRAINT clients_therapist_ref_fkey FOREIGN KEY (therapist_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;
ALTER TABLE clients
    ADD CONSTRAINT clients_phone_number_key UNIQUE (therapist_ref, phone_number);

ALTER TABLE therapeutic_tasks
    DROP COLUMN owner;
ALTER TABLE therapeutic_tasks
    ALTER COLUMN owner_ref SET NOT NULL;
ALTER TABLE therapeutic_tasks
    ADD CONSTRAINT therapeutic_tasks_owner_ref_fkey FOREIGN KEY (owner_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;
CREATE UNIQUE INDEX therapeutic_tasks_owner_name_key ON therapeutic_tasks (owner_ref, LOWER(name));

ALTER TABLE appointment_types
    DROP COLUMN owner_ref;
ALTER TABLE appointment_types
    RENAME COLUMN new_owner_ref TO owner_ref;
ALTER TABLE appointment_types
    ALTER COLUMN owner_ref SET NOT NULL;
ALTER TABLE appointment_types
    ADD CONSTRAINT appointment_types_owner_ref_fkey FOREIGN KEY (owner_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;

ALTER TABLE programs
    DROP COLUMN owner_ref;
ALTER TABLE programs
    RENAME COLUMN new_owner_ref TO owner_ref;
ALTER TABLE programs
    ALTER COLUMN owner_ref SET NOT NULL;
ALTER TABLE programs
    ADD CONSTRAINT programs_owner_ref_fkey FOREIGN KEY (owner_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;

ALTER TABLE exercises
    DROP COLUMN therapist_id;
ALTER TABLE exercises
    ALTER COLUMN owner_ref SET NOT NULL;
ALTER TABLE exercises
    ADD CONSTRAINT exercises_owner_ref_fkey FOREIGN KEY (owner_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;

ALTER TABLE appointments
    DROP COLUMN therapist_ref;
ALTER TABLE appointments
    RENAME COLUMN new_therapist_ref TO therapist_ref;
ALTER TABLE appointments
    ALTER COLUMN therapist_ref SET NOT NULL;
ALTER TABLE appointments
    ADD CONSTRAINT appointments_therapist_ref_fkey FOREIGN KEY (therapist_ref) REFERENCES therapists (new_id) ON DELETE CASCADE;

ALTER TABLE therapists
    DROP COLUMN id;
ALTER TABLE therapists
    RENAME COLUMN new_id TO id;
ALTER TABLE therapists
    ADD CONSTRAINT therapists_user_id_fkey FOREIGN KEY (id) REFERENCES users (new_id) ON DELETE CASCADE;

ALTER TABLE users
    DROP COLUMN id;
ALTER TABLE users
    RENAME COLUMN new_id TO id;
