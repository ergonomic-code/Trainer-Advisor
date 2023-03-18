create table clients
(
    id                  bigint generated always as identity primary key,
    first_name          varchar     not null,
    last_name         varchar     not null,
    patronymic          varchar     not null,
    birth_date          date        not null,
    phone_number        varchar(18) not null unique,
    diagnose            varchar     not null,
    email               varchar unique,
    distribution_source varchar
);

create table appointments
(
    id             bigint generated always as identity primary key,
    datetime       timestamp not null,
    complaints     varchar   not null,
    photo          varchar,
    client_id      bigint    not null references clients on delete cascade,
    program_id     bigint    not null references programs on delete cascade,
    therapist_id   bigint    not null references therapists on delete cascade,
    payed          bool,
    payment_method varchar
);

CREATE UNIQUE INDEX unique_therapist_appointments_idx ON appointments (therapist_id, date_trunc('minute', datetime))