create table persons
(
    id          bigint auto_increment
        primary key,
    name        varchar(255) not null,
    column_name varchar(10)  not null
);