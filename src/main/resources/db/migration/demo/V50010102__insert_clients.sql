insert into clients (first_name, middle_name, last_name, birth_date, phone_number, email, distribution_source,
                     complaints, therapist_id,
                     created_at, version)
VALUES ('Иван', 'Иванович', 'Иванов', '1970.01.01', '+79231233445', 'ivanoff@ya.ru', '', 'Сколеоз', 2, now(), 1),
       ('Пётр', 'Петрович', 'Петров', '1978.01.01', '+79231233446', 'petrov@ya.ru', '', 'Гипертоническая болезнь', 2,
        now(), 1),
       ('Сергей', 'Иванович', 'Сергеев', '1990.01.01', '+79231233447', 'sergeev@ya.ru', '', 'Травма левого мениска', 2,
        now(), 1);