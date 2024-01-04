ALTER TABLE files
    ADD COLUMN bucket VARCHAR;

UPDATE files
SET bucket = 'exerciseSteps';

ALTER TABLE files
    ALTER COLUMN bucket SET NOT NULL;