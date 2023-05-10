-- Первоначальный код писался студентами, за которыми я недоглядел и миграции уже ушли в прод, поэтому приходится
-- править проблемы отдельными миграциями

-- images

ALTER TABLE exercise_steps
    RENAME COLUMN photo TO image_id;

ALTER TABLE exercise_steps
    ALTER COLUMN image_id TYPE BIGINT USING NULL;

ALTER TABLE exercise_steps
    ADD FOREIGN KEY (image_id) REFERENCES images;

-- steps

ALTER TABLE exercise_steps
    ADD COLUMN step_index SMALLINT;

UPDATE exercise_steps
SET step_index = se.exercise_step
FROM (SELECT id, row_number() OVER (PARTITION BY exercise_id ORDER BY id) exercise_step
      FROM exercise_steps) as se
WHERE exercise_steps.id = se.id;

ALTER TABLE exercise_steps
    ALTER COLUMN step_index SET NOT NULL,
    ADD CONSTRAINT exercise_steps_id_step_index_key UNIQUE (id, step_index);
