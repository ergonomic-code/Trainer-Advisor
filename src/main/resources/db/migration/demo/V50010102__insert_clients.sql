insert into clients (first_name, middle_name, last_name, birth_date, phone_number, email, distribution_source_type,
                     distribution_source_comment,
                     complaints, therapist_id, created_at, version)
VALUES ('Яна', null, 'Бортник', '1990.01.01', '+79231233445', 'bortnik@ya.ru', 'SOCIAL_NETWORKS', null,
        'Боль в пояснице слева, мышечный спазм.', 2, now(), 1),

-- ---

       ('Любовь', null, 'Королёва', null, '+79139282181', null, 'OTHER', 'Клиент Forpost yoga',
        'Слабость мтд', 2,
        now(), 1),

-- ---

       ('Варвара', null, 'Сергеева', '1990.01.01', '+79193927491', 'sergeev@ya.ru', null, null,
        'Боль в шее, лопатках.
Сильный вальгус голеностопа.
С-образный левосторонний сколиоз.
Крыловидные лопатки.
Натяжение нервов по задней поверхности ног.
Астеничное телосложение.', 2,
        now(), 1);