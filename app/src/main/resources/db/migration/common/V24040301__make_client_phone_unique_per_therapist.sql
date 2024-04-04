ALTER TABLE clients
    DROP CONSTRAINT clients_phone_number_key;

ALTER TABLE clients
    ADD CONSTRAINT clients_phone_number_key UNIQUE (therapist_id, phone_number);