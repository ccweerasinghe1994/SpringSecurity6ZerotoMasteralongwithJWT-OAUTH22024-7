insert ignore into `users`
values ('user', '{noop}Sliit123!@#45', '1');
insert ignore into `authorities`
values ('user', 'read');

insert ignore into `users`
values ('admin', '{bcrypt}$2a$12$X8LD1B5so/NckWWAY/47j.3G0O8v1btdhm3SkM6MZZtzmn9lqkvuO', '1');
insert ignore into `authorities`
values ('admin', 'admin');