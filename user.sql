create table user
(
    id          int auto_increment
        primary key,
    name        varchar(32)                         null,
    age         int                                 null,
    create_date timestamp default CURRENT_TIMESTAMP null,
    update_date timestamp default CURRENT_TIMESTAMP null
);