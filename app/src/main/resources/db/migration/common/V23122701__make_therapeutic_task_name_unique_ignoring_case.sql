ALTER TABLE therapeutic_tasks
    DROP CONSTRAINT therapeutic_tasks_owner_name_key;

CREATE UNIQUE INDEX therapeutic_tasks_owner_name_key ON therapeutic_tasks (owner, LOWER(name));