create table public.clients
(
    _name               varchar not null,
    secondname          varchar,
    surname             varchar not null,
    birth_date          date    not null,
    phone_number        varchar not null,
    diagnose            varchar not null,
    email               varchar,
    distribution_sourse varchar,
    id                  bigint generated always as identity
            primary key
);

create table public.appointment
(
    id                  bigint generated always as identity
            primary key,
    date_app date not null,
    client_id     BIGINT NOT NULL
        CONSTRAINT fk_appointments_clients_ids REFERENCES clients
            ON DELETE CASCADE

);