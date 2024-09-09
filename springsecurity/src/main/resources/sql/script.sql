create table users
(
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    enabled  boolean      not null
);
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);


-- Insert the first user into the users table
INSERT INTO users (username, password, enabled)
VALUES ('user', '{noop}Sliit123!@#qq11zsa@fd', '1');

-- Insert the second user into the users table
INSERT INTO users (username, password, enabled)
VALUES ('admin', '{bcrypt}$2y$14$OPivb1UNmmrSYTo5OQWDnuAZ78cS9DBCV5S9SsuWroQ10.wtm9JH6', '1');


INSERT INTO authorities (username, authority)
VALUES ('user', 'read');
INSERT INTO authorities (username, authority)
VALUES ('admin', 'admin');

create table `customer`
(
    `id`    int          not null auto_increment primary key,
    `email` varchar(45)  not null,
    `pwd`   varchar(200) not null,
    `role`  varchar(45)  not null
);


insert into customer (email, pwd, role)
values ('user@abcbank.com', '{noop}Sliit123!@#qq11zsa@fd', 'read');
insert into customer (email, pwd, role)
values ('admin@abcbank.com', '{bcrypt}$2y$14$OPivb1UNmmrSYTo5OQWDnuAZ78cS9DBCV5S9SsuWroQ10.wtm9JH6', 'admin');