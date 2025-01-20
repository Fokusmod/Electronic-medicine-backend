 --liquibase formatted sql

 --changeset WowVendorTeamHelper:insert_data_role

insert into users (email, password)
values ('admin', '$2a$12$cVRhalRnnzRlozR2ixuqLuiXlJks2fM7F56gAE3/kgQEYKAqeehlK'),
       ('user', '$2a$12$3/3pHUymH7E/H/8g5eHQBOtuLu0jn3Ieki8eJtD9HW.e7VyJ.F9AC');
