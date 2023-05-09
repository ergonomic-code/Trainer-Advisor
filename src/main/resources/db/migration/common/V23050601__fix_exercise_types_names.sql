-- Первоначальный код писался студентами, за которыми я недоглядел и миграции уже ушли в прод, поэтому приходится
-- править проблемы отдельными миграциями

UPDATE exercise_types
SET name = 'Разминка'
WHERE id = 1;
UPDATE exercise_types
SET name = 'Мобилизация'
WHERE id = 2;
UPDATE exercise_types
SET name = 'Укрепление'
WHERE id = 3;
UPDATE exercise_types
SET name = 'Вытяжение'
WHERE id = 4;
UPDATE exercise_types
SET name = 'Расслабление'
WHERE id = 5;
UPDATE exercise_types
SET name = 'Тракция'
WHERE id = 6;