-- Первоначальный код писался студентами, за которыми я недоглядел и миграции уже ушли в прод, поэтому приходится
-- править проблемы отдельными миграциями

ALTER TABLE therapeutic_purposes
    ADD CONSTRAINT therapeutic_purposes_purpose_key UNIQUE (purpose),
    ALTER COLUMN purpose SET NOT NULL;
