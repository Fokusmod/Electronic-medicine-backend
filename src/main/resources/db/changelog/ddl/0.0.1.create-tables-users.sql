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

 -- changeset MedicineService:create-tables

  create table roles
  (
      id                bigserial primary key,
      title             varchar(50) not null
  );

  create table specialities
  (
     id                 bigserial primary key,
     title              varchar(50) not null
  );

   create table users_roles
   (
       user_id bigint not null,
       role_id bigint not null,
       primary key (user_id, role_id),
       foreign key (user_id) references users (id),
       foreign key (role_id) references roles (id)
   );

   create table users_specialities
    (
        user_id bigint not null,
        speciality_id bigint not null,
        primary key (user_id, speciality_id),
        foreign key (user_id) references users (id),
        foreign key (speciality_id) references specialities (id)
    );

-- changeset MedicineService:create-activate-column

    ALTER TABLE users
    ADD activation_code varchar(200);

-- changeset MedicineService:more-info-users

    create table positions
    (
       id                 bigserial primary key,
       title              varchar(50) not null
    );

    create table statuses
    (
       id                 bigserial primary key,
       title              varchar(50) not null
    );

    ALTER TABLE users
    ADD age int, ADD first_name varchar(50), ADD last_name varchar(70),
    ADD experience date,
    ADD status_id bigint references statuses (id),
    ADD position_id bigint references positions (id);

-- changeset MedicineService:change-user-info-type

    ALTER TABLE users
    ALTER COLUMN age SET default 0,
    ALTER COLUMN first_name SET default NULL,
    ALTER COLUMN last_name SET default NULL,
    ALTER COLUMN experience SET default NULL;

-- changeset MedicineService:create-review

    create table reviews
    (
       id                 bigserial primary key,
       author             varchar(120) not null,
       message            text not null
    );

-- changeset MedicineService:create-review-relation

    create table users_reviews
   (
       user_id bigint not null,
       review_id bigint not null,
       primary key (user_id, review_id),
       foreign key (user_id) references users (id),
       foreign key (review_id) references reviews (id)
   );

-- changeset MedicineService:create-educations

create table educations
    (
       id               bigserial primary key,
       title            text not null
    );

 create table users_educations
   (
       user_id bigint not null,
       education_id bigint not null,
       primary key (user_id, education_id),
       foreign key (user_id) references users (id),
       foreign key (education_id) references educations (id)
   );

-- changeset MedicineService:add-users-photo-url

create table photos
(
    id               bigserial primary key,
    title            text not null
);

ALTER TABLE users
    ADD photo_id bigint references photos (id);

-- changeset MedicineService:create-reception-tables

create table receptions
(
    id                  bigserial primary key,
    reception_time      date
);

create table reception_patients
(
       user_id bigint not null,
       specialist_id bigint not null,
       primary key (user_id, specialist_id),
       foreign key (user_id) references users (id),
       foreign key (specialist_id) references users (id)
);

-- changeset MedicineService:change-reception-table

ALTER table receptions
Alter COLUMN reception_time type TIMESTAMP WITHOUT TIME ZONE;

-- changeset MedicineService:change-reception_patients-table

Alter table reception_patients
DROP column specialist_id,
Add COLUMN reception_id bigint not null references receptions (id);



