INSERT INTO appointment_types
    (owner_ref, name, created_at, version)
VALUES (2, 'Тренировка', now(), 1),
       (2, 'Консультация', now(), 1),
       (2, 'Диагностика', now(), 1);

CREATE OR REPLACE FUNCTION appnt_type(type_name varchar) RETURNS BIGINT
AS
'SELECT id
 FROM appointment_types
 WHERE name = type_name'
    LANGUAGE SQL
    IMMUTABLE
    RETURNS NULL ON NULL INPUT;

INSERT INTO appointments
(therapist_ref, client_ref, type_ref, therapeutic_task_ref, date_time, time_zone, place, cost, payed, comment,
 created_at, modified_at, version)
VALUES (2, client('Бортник'), appnt_type('Тренировка'), thrp_task('Мобилизация ПОП'),
        current_date + '9 hours 30 minutes'::INTERVAL, current_setting('TIMEZONE'), 'Freedom Yoga', 2000, false, null,
        now(), null, 0),
       (2, client('Королёва'), appnt_type('Консультация'), thrp_task('ЙТ гастрита'),
        current_date + '11 hours 15 minutes'::INTERVAL, current_setting('TIMEZONE'), 'Онлайн', 18000, true, null,
        now(), null, 0),

       (2, client('Сергеева'), appnt_type('Диагностика'), thrp_task('Укрепление МТД'),
        current_date + '2 days 13 hours 00 minutes'::INTERVAL, current_setting('TIMEZONE'), null, 1000, false, null,
        now(), null, 0),

       (2, client('Бортник'), appnt_type('Диагностика'), thrp_task('Мобилизация ПОП'),
        current_date + '6 days 10 hours 45 minutes'::INTERVAL, current_setting('TIMEZONE'),
        'Земля, Российская Федерация, Новосибирская обл., р.п. Кольцово, Рассветная 10-15', 2000, false, null,
        now(), null, 0),

       (2, client('Бортник'), appnt_type('Тренировка'), thrp_task('Мобилизация ПОП'),
        current_date + '7 days 9 hours 30 minutes'::INTERVAL, current_setting('TIMEZONE'), 'Дома', 2000, false, null,
        now(), null, 0),

       (2, client('Королёва'), appnt_type('Тренировка'), null,
        current_date + '7 days 11 hours 15 minutes'::INTERVAL, current_setting('TIMEZONE'), null, 9000, true, null,
        now(), null, 0)
;