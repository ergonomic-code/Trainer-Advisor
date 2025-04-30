INSERT INTO appointment_types
    (owner_ref, name, created_at, version)
VALUES (usr('therapist@trainer-advisor.pro'), 'Тренировка', now(), 1),
       (usr('therapist@trainer-advisor.pro'), 'Консультация', now(), 1),
       (usr('therapist@trainer-advisor.pro'), 'Диагностика', now(), 1);

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
 created_at, modified_at, version, duration, status)
VALUES (usr('therapist@trainer-advisor.pro'), client('Бортник'), appnt_type('Тренировка'),
        thrp_task('therapist@trainer-advisor.pro', 'Мобилизация ПОП'),
        current_date - '6 days 9 hours 30 minutes'::INTERVAL, 'Asia/Novosibirsk', 'Freedom Yoga', 2000, false,
        null,
        now(), null, 1, '1 HOUR', 'PENDING'),
       (usr('therapist@trainer-advisor.pro'), client('Королёва'), appnt_type('Консультация'),
        thrp_task('therapist@trainer-advisor.pro', 'ЙТ гастрита'),
        current_date - '6 days 11 hours 15 minutes'::INTERVAL, 'Europe/Moscow', 'Онлайн', 18000, true, null,
        now(), null, 1, '1 HOUR', 'PENDING'),

       (usr('therapist@trainer-advisor.pro'), client('Бортник'), appnt_type('Тренировка'),
        thrp_task('therapist@trainer-advisor.pro', 'Мобилизация ПОП'),
        current_date + '9 hours 30 minutes'::INTERVAL, 'Asia/Novosibirsk', 'Freedom Yoga', 2000, false, null,
        now(), null, 1, '1 HOUR', 'PENDING'),
       (usr('therapist@trainer-advisor.pro'), client('Королёва'), appnt_type('Консультация'),
        thrp_task('therapist@trainer-advisor.pro', 'ЙТ гастрита'),
        current_date + '11 hours 15 minutes'::INTERVAL, 'Europe/Moscow', 'Онлайн', 18000, true, null,
        now(), null, 1, '1 HOUR', 'PENDING'),

       (usr('therapist@trainer-advisor.pro'), client('Сергеева'), appnt_type('Диагностика'),
        thrp_task('therapist@trainer-advisor.pro', 'Укрепление МТД'),
        current_date + '2 days 13 hours 00 minutes'::INTERVAL, current_setting('TIMEZONE'), null, 1000, false, null,
        now(), null, 1, '1 HOUR', 'PENDING'),

       (usr('therapist@trainer-advisor.pro'), client('Бортник'), appnt_type('Диагностика'),
        thrp_task('therapist@trainer-advisor.pro', 'Мобилизация ПОП'),
        current_date + '6 days 10 hours 45 minutes'::INTERVAL, 'Asia/Novosibirsk',
        'Земля, Российская Федерация, Новосибирская обл., р.п. Кольцово, Рассветная 10-15', 2000, false, null,
        now(), null, 1, '1 HOUR', 'PENDING'),

       (usr('therapist@trainer-advisor.pro'), client('Бортник'), appnt_type('Тренировка'),
        thrp_task('therapist@trainer-advisor.pro', 'Мобилизация ПОП'),
        current_date + '7 days 9 hours 30 minutes'::INTERVAL, 'Asia/Novosibirsk', 'Дома', 2000, false, null,
        now(), null, 1, '1 HOUR', 'PENDING'),

       (usr('therapist@trainer-advisor.pro'), client('Королёва'), appnt_type('Тренировка'), null,
        current_date + '7 days 11 hours 15 minutes'::INTERVAL, 'Europe/Moscow', null, 9000, true, null,
        now(), null, 1, '1 HOUR', 'PENDING')
;