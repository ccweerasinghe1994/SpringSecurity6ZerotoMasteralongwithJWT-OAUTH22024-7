drop table if exists `users`;
drop table if exists `authorities`;


create table if not exists `customer`
(
    `id`    int          not null auto_increment,
    `email` varchar(45)  not null,
    `pwd`   varchar(200) not null,
    `role`  varchar(45)  not null,
    primary key (`id`)
);

insert into `customer` (`email`, `pwd`, `role`)
values ('user@abc.com', '{noop}Sliit123!@##$', 'read');

insert into `customer` (`email`, `pwd`, `role`)
values ('admin@abc.com', '{bcrypt}$2a$12$X8LD1B5so/NckWWAY/47j.3G0O8v1btdhm3SkM6MZZtzmn9lqkvuO', 'admin');