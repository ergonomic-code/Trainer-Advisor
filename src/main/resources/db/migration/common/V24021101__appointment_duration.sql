ALTER TABLE appointments
    ADD COLUMN duration INTERVAL;

UPDATE appointments
SET duration = '1 HOUR'::INTERVAL;

ALTER TABLE appointments
    ALTER COLUMN duration SET NOT NULL;