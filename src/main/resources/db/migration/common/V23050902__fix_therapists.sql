-- Первоначальный код писался студентами, за которыми я недоглядел и миграции уже ушли в прод, поэтому приходится
-- править проблемы отдельными миграциями

ALTER TABLE therapists
    ALTER id DROP IDENTITY;

-- noinspection SqlWithoutWhere
UPDATE therapists
set id = user_id;

ALTER TABLE therapists
    DROP COLUMN user_id;

ALTER TABLE therapists
    ADD FOREIGN KEY (id) REFERENCES users;