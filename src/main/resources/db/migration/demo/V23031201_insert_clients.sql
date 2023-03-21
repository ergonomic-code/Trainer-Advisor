insert into clients (first_name, patronymic, last_name, birth_date, phone_number, diagnose, email, distribution_source)
VALUES ('Иван', 'Иванович', 'Иванов', '1970.01.01', '+79231233445', 'Малярия', 'ivanoff@ya.ru', null),
       ('Пётр', 'Петрович', 'Петров', '1978.01.01', '+79231233446', 'Корь', 'petrov@ya.ru', null),
       ('Сергей', 'Иванович', 'Сергеев', '1990.01.01', '+79231233447', 'Краснуха', 'sergeev@ya.ru', null);

insert into appointments (datetime, complaints, photo, client_id, program_id, therapist_id, payed)
VALUES ('2023-03-18 04:05:00', 'Болит пальчик', null, (select id from clients where email = 'ivanoff@ya.ru'),
        (select id from programs limit 1), (select id from therapists limit 1), false),
       ('2023-03-19 04:05:00', 'Другой пальчик', null, (select id from clients where email = 'ivanoff@ya.ru'),
        (select id from programs limit 1), (select id from therapists limit 1), false),
       ('2023-03-18 06:05:00', 'Болит ножка', null, (select id from clients where email = 'petrov@ya.ru'),
        (select id from programs limit 1), (select id from therapists limit 1), false),
       ('2023-03-28 06:05:00', 'Болит животик', null, (select id from clients where email = 'sergeev@ya.ru'),
        (select id from programs limit 1), (select id from therapists limit 1), false);


