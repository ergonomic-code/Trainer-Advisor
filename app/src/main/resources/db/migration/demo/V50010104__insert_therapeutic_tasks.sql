INSERT INTO therapeutic_tasks
    (name, owner, created_at, version)
VALUES ('Мобилизация ПОП', 2, now(), 1),
       ('Снятие компрессии с ПОП', 2, now(), 1),
       ('Снятие напряжения с ШОП', 2, now(), 1),
       ('Коррекция кифолордотической осанки', 2, now(), 1),
       ('ЙТ гастрита', 2, now(), 1),
       ('ЙТ травмы медиального мениска', 2, now(), 1),
       ('Коррекция С-образного сколеоза', 2, now(), 1),
       ('Коррекция жёсткости ГОП', 2, now(), 1),
       ('Коррекция плоскостопия', 2, now(), 1),
       ('Коррекция протракции головы', 2, now(), 1),
       ('Деротация ГОП', 2, now(), 1),
       ('Терапия ГЭРБ', 2, now(), 1),
       ('Терапия гипертончиской болезни', 2, now(), 1),
       ('Терапия атеросклероза', 2, now(), 1),
       ('Укреление МТД', 2, now(), 1),
       ('ЙТ боли в пояснице', 2, now(), 1)
;

CREATE OR REPLACE FUNCTION thrp_task(task_name varchar) RETURNS BIGINT
AS
'SELECT id
 FROM therapeutic_tasks
 WHERE name = task_name'
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT;
