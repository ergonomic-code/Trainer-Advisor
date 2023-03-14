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
    id        bigint generated always as identity
        primary key,
    date_app  date   not null,
    time_app time not null,
    comlaints varchar not null,
    photo varchar,
    client_id bigint not null
        constraint fk_appointments_clients_ids
            references clients
            on delete cascade,

    program_id bigint not null
        constraint fk_appointments_programs_ids
            references programs
            on delete cascade,

    therapist_id bigint not null
        constraint fk_appointments_therapist_ids
            references therapists
            on delete cascade ,
    payment bool,
    payment_method varchar
);