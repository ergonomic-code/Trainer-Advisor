ALTER TABLE appointments
    ADD COLUMN status VARCHAR;

UPDATE appointments
SET status = 'PENDING';

ALTER TABLE appointments
    ALTER COLUMN status SET NOT NULL;