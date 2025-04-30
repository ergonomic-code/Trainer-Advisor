TRUNCATE therapeutic_tasks, journal_entries, client_files, appointments, programs, program_exercises;

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
       ('Коррекция осанки', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Деротация ГОП', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия ГЭРБ', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия гипертонической болезни', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Терапия атеросклероза', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Укрепление МТД', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('ЙТ боли в пояснице', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Реабилитация после травмы колена', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Подготовка к беговому сезону', usr('therapist@trainer-advisor.pro'), now(), 1),
       ('Релаксационный комплекс', usr('therapist@trainer-advisor.pro'), now(), 1)
;

CREATE OR REPLACE FUNCTION thrp_task(therapist_login varchar, task_name varchar) RETURNS BIGINT
AS
'SELECT tt.id
 FROM therapeutic_tasks tt
          JOIN therapists t on tt.owner_ref = t.id
 WHERE tt.owner_ref = usr(therapist_login)
   AND name = task_name'
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT;
