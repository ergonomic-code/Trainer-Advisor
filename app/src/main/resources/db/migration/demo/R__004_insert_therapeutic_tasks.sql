INSERT INTO therapeutic_tasks
    (name, owner_ref, created_at, version)
VALUES ('Мобилизация ПОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Снятие компрессии с ПОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Снятие напряжения с ШОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Коррекция кифолордотической осанки', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('ЙТ гастрита', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('ЙТ травмы медиального мениска', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Коррекция С-образного сколеоза', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Коррекция жёсткости ГОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Коррекция плоскостопия', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Коррекция протракции головы', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Деротация ГОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия ГЭРБ', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия гипертонической болезни', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия атеросклероза', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Укрепление МТД', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('ЙТ боли в пояснице', usr('therapist@trainer-advisor.pro'), now(), 1)
;

CREATE OR REPLACE FUNCTION thrp_task(task_name varchar) RETURNS BIGINT
AS
'SELECT id
 FROM therapeutic_tasks
 WHERE name = task_name'
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT;
