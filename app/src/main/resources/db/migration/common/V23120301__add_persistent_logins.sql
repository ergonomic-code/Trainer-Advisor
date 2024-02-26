CREATE TABLE persistent_logins
(
    series    varchar(64) PRIMARY KEY,
    username  VARCHAR(64) NOT NULL,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
);