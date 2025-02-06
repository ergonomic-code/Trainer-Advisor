UPDATE clients
SET phone_number = '+' || REGEXP_REPLACE(phone_number, '[^0-9]', '', 'g');

ALTER TABLE clients
    ADD CONSTRAINT phone__number_e164_check
        CHECK (
            LENGTH(phone_number) BETWEEN 12 AND 14 -- +(x[xx])(xxxxxxxxxx)
                AND phone_number ~ '^\+\d{1,3}\d{10}$' -- пока затачиваемся на Российские номера
            );