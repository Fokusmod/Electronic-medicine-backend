 --liquibase formatted sql

 --changeset WowVendorTeamHelper:create_tables

 create table users
 (
     id                bigserial primary key,
     email             varchar(150),
     password          varchar(250) not null,
     created_at        timestamp default current_timestamp,
     updated_at        timestamp default current_timestamp
 );