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
        constraint id
            primary key
);