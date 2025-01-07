ALTER TABLE clients
    ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
-- noinspection SqlWithoutWhere
UPDATE clients t
SET new_id = gen_random_uuid();
ALTER TABLE clients
    DROP CONSTRAINT clients_pkey CASCADE;
ALTER TABLE clients
    ADD PRIMARY KEY (new_id);

ALTER TABLE journal_entries
    ADD COLUMN client_ref UUID;
UPDATE journal_entries je
SET client_ref = c.new_id
FROM clients c
WHERE c.id = je.client;

ALTER TABLE client_files
    ADD COLUMN new_client_ref UUID;
UPDATE client_files cf
SET new_client_ref = c.new_id
FROM clients c
WHERE c.id = cf.client_ref;

ALTER TABLE appointments
    ADD COLUMN new_client_ref UUID;
UPDATE appointments a
SET new_client_ref = c.new_id
FROM clients c
WHERE c.id = a.client_ref;

ALTER TABLE journal_entries
    DROP COLUMN client;
ALTER TABLE journal_entries
    ALTER COLUMN client_ref SET NOT NULL;
ALTER TABLE journal_entries
    ADD CONSTRAINT journal_entries_client_ref_fkey FOREIGN KEY (client_ref) REFERENCES clients (new_id) ON DELETE CASCADE;
ALTER TABLE journal_entries
    ADD CONSTRAINT journal_entries_client_date_key UNIQUE (client_ref, date);

ALTER TABLE client_files
    DROP COLUMN client_ref;
ALTER TABLE client_files
    RENAME COLUMN new_client_ref TO client_ref;
ALTER TABLE client_files
    ALTER COLUMN client_ref SET NOT NULL;
ALTER TABLE client_files
    ADD CONSTRAINT client_files_client_ref_fkey FOREIGN KEY (client_ref) REFERENCES clients (new_id) ON DELETE CASCADE;

ALTER TABLE appointments
    DROP COLUMN client_ref;
ALTER TABLE appointments
    RENAME COLUMN new_client_ref TO client_ref;
ALTER TABLE appointments
    ALTER COLUMN client_ref SET NOT NULL;
ALTER TABLE appointments
    ADD CONSTRAINT appointments_client_ref_fkey FOREIGN KEY (client_ref) REFERENCES clients (new_id) ON DELETE CASCADE;

ALTER TABLE clients
    DROP COLUMN id;
ALTER TABLE clients
    RENAME COLUMN new_id TO id;