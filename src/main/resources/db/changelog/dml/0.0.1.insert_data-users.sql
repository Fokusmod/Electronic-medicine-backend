 --liquibase formatted sql

 --changeset WowVendorTeamHelper:insert_data_role

insert into users (email, password)
values ('admin', '$2a$12$cVRhalRnnzRlozR2ixuqLuiXlJks2fM7F56gAE3/kgQEYKAqeehlK'),
       ('user', '$2a$12$3/3pHUymH7E/H/8g5eHQBOtuLu0jn3Ieki8eJtD9HW.e7VyJ.F9AC');

 --changeset MedicineService:insert_data

 insert into roles (title)
 values ('ADMIN'), ('SPECIALIST'), ('USER');

 insert into specialities (title)
 values ('Кардиолог'), ('Стоматолог'), ('Терапевт'), ('Окулист'), ('Онколог'), ('Гинеколог'), ('Психолог'),
 ('Психиатр') ,('Нарколог'), ('Уролог'), ('Ренгенолог'), ('Хирург'), ('none');

 --changeset MedicineService:insert_data-roles-and-specialities

 insert into users_roles (user_id, role_id)
 values ('1', '1'), ('2', '3');

 insert into users_specialities (user_id, speciality_id)
 values ('1', '13'), ('2', '13');


 --changeset MedicineService:insert_data-statuses-positions

 insert into statuses (title)
  values ('IN_WORK'), ('DAY_OFF'), ('VACATION'), ('ONLINE'), ('OFFLINE');

  insert into positions (title)
  values ('DOCTOR'), ('ADMINISTRATION'), ('SUB_ADMINISTRATION'), ('HEAD_DOCTOR'), ('SUB_HEAD_DOCTOR');

 --changeset MedicineService:insert-default-specialist

 insert into users (email, password, first_name, last_name, age, experience, activation_code, status_id, position_id)
 values ('Doctor@kardiolog', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Анна', 'Викторовна', 27, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR'));

 --changeset MedicineService:insert-default-more-specialists

 insert into users (email, password, first_name, last_name, age, experience, activation_code, status_id, position_id)
  values ('Doctor@onkolog', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Элла', 'Новикова', 25, '2021-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR')),
  ('Doctor@okulist', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Денис', 'Прохоров', 28, '2019-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR')),
  ('Doctor@terapevt', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Владислав', 'Семенов', 27, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR')),
  ('Doctor@stometolog', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Виктория', 'Войтина', 24, '2022-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR')),
  ('Doctor@psiholog', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Михаил', 'Белов', 32, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'DOCTOR')),

  ('Doctor@admin', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Анна', 'Смирнова', 45, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'ADMINISTRATION')),
  ('Doctor@head-doctor', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Екатерина', 'Иванова', 39, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'HEAD_DOCTOR')),
  ('Doctor@sun-head-doctor', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Мария', 'Петрова', 23, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'SUB_HEAD_DOCTOR')),
  ('Doctor@admin2', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Ольга', 'Васильева', 33, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'ADMINISTRATION')),
  ('Doctor@admin3', '$2a$12$nP2RyULdHeZSRFxY250lJe7.9.Ux8zGUbyzc.nsRkfe/B/wZszIDe', 'Наталья', 'Кузнецова', 31, '2020-03-14', null, (SELECT id FROM statuses WHERE title = 'IN_WORK'), (SELECT id FROM positions WHERE title = 'ADMINISTRATION'));

 --changeset MedicineService:insert-default-specialist-role

 insert into users_roles (user_id, role_id)
 values ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
 ((SELECT id FROM users WHERE email = 'Doctor@onkolog'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
 ((SELECT id FROM users WHERE email = 'Doctor@okulist'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
 ((SELECT id FROM users WHERE email = 'Doctor@stometolog'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),

 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM roles WHERE title = 'ADMIN')),
 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM roles WHERE title = 'ADMIN')),
 ((SELECT id FROM users WHERE email = 'Doctor@sun-head-doctor'), (SELECT id FROM roles WHERE title = 'ADMIN')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin2'), (SELECT id FROM roles WHERE title = 'ADMIN')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM roles WHERE title = 'ADMIN'));

 insert into users_specialities (user_id, speciality_id)
 values ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM specialities WHERE title = 'Кардиолог')),
 ((SELECT id FROM users WHERE email = 'Doctor@onkolog'), (SELECT id FROM specialities WHERE title = 'Онколог')),
 ((SELECT id FROM users WHERE email = 'Doctor@okulist'), (SELECT id FROM specialities WHERE title = 'Окулист')),
 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM specialities WHERE title = 'Терапевт')),
 ((SELECT id FROM users WHERE email = 'Doctor@stometolog'), (SELECT id FROM specialities WHERE title = 'Стоматолог')),
 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM specialities WHERE title = 'Психолог')),

 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM specialities WHERE title = 'Гинеколог')),
 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM specialities WHERE title = 'Нарколог')),
 ((SELECT id FROM users WHERE email = 'Doctor@sun-head-doctor'), (SELECT id FROM specialities WHERE title = 'Ренгенолог')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin2'), (SELECT id FROM specialities WHERE title = 'Хирург')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM specialities WHERE title = 'Психиатр'));


 --changeset MedicineService:insert-default-specialist-educations

 insert into educations (title) values
 ('Российский национальный исследовательский медицинский университет имени Н. И. Пирогова'),
 ('Санкт-Петербургский государственный медицинский университет имени академика И. П. Павлова'),
 ('Казанский федеральный университет (КФУ) - Факультет медицины'),
 ('Государственный медицинский университет имени Сеченова'),
 ('Томский государственный университет'),
 ('Уральский государственный медицинский университет'),
 ('Новосибирский государственный университет (факультет медицины и биомедицинских технологий)'),
 ('Волгоградский государственный медицинский университет'),
 ('Калининградский государственный университет (медицинский факультет)'),
 ('Челябинская государственная медицинская академия'),
 ('Московский государственный медико-стоматологический университет'),
 ('Рязанский государственный медицинский университет'),
 ('Северный государственный медицинский университет'),
 ('Азовский государственный медицинский университет'),
 ('Орловский государственный университет'),
 ('Ставропольский государственный медицинский университет'),
 ('Крымский федеральный университет (медицинский факультет)'),
 ('Тверской государственный медицинский университет'),
 ('Бурятский государственный университет (медицинский факультет)'),
 ('Кубанский государственный медицинский университет'),

 ('Российская медицинская академия последипломного образования (РМАПО)'),
 ('Федеральный медицинский исследовательский центр имени В. А. Алмазова'),
 ('Национальный медицинский исследовательский центр терапии и профилактической медицины'),
 ('Институт усовершенствования врачей (ИУВ) при МГМУ им. И. М. Сеченова'),
 ('Центр повышения квалификации работников здравоохранения'),
 ('Российская централизованная медицинская ассоциация (РЦМА)'),
 ('Клинический институт повышения квалификации специалистов здравоохранения'),
 ('Московский научно-практический центр психиатроии'),
 ('Научно-медицинский центр по охране здоровья семьи и репродукции человека'),
 ('Общероссийская общественная организация "Ассоциация специалистов в области медицины'),
 ('Сибирский государственный медицинский университет (факультет повышения квалификации)'),
 ('Южноуральский государственный медицинский университет (кафедра повышения квалификации)'),
 ('Институт повышения квалификации и переподготовки кадров в здравоохранении'),
 ('Учебный центр повышения квалификации медицинских работников'),
 ('Институт медицинского образования и повышения квалификации'),
 ('Научный центр здоровья детей (повышение квалификации педиатров)'),
 ('Национальный исследовательский институт общественного здоровья'),
 ('Учебный центр по обучению и повышению квалификации в области здравоохранения'),
 ('Станция скорой медицинской помощи (курсы повышения квалификации водителей и медиков)'),
 ('Национальная ассоциация медицинских сестер (курсы повышения квалификации)');


 insert into users_educations (user_id, education_id) values
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Российский национальный исследовательский медицинский университет имени Н. И. Пирогова')),
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Институт повышения квалификации и переподготовки кадров в здравоохранении')),
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Научный центр здоровья детей (повышение квалификации педиатров)')),
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Южноуральский государственный медицинский университет (кафедра повышения квалификации)')),
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Клинический институт повышения квалификации специалистов здравоохранения')),
 ((SELECT id FROM users WHERE email = 'Doctor@kardiolog'), (SELECT id FROM educations WHERE title = 'Уральский государственный медицинский университет')),

 ((SELECT id FROM users WHERE email = 'Doctor@onkolog'), (SELECT id FROM educations WHERE title = 'Азовский государственный медицинский университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@onkolog'), (SELECT id FROM educations WHERE title = 'Общероссийская общественная организация "Ассоциация специалистов в области медицины')),
 ((SELECT id FROM users WHERE email = 'Doctor@onkolog'), (SELECT id FROM educations WHERE title = 'Национальная ассоциация медицинских сестер (курсы повышения квалификации)')),

 ((SELECT id FROM users WHERE email = 'Doctor@okulist'), (SELECT id FROM educations WHERE title = 'Северный государственный медицинский университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@okulist'), (SELECT id FROM educations WHERE title = 'Учебный центр повышения квалификации медицинских работников')),

 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM educations WHERE title = 'Научный центр здоровья детей (повышение квалификации педиатров)')),
 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM educations WHERE title = 'Волгоградский государственный медицинский университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM educations WHERE title = 'Казанский федеральный университет (КФУ) - Факультет медицины')),
 ((SELECT id FROM users WHERE email = 'Doctor@terapevt'), (SELECT id FROM educations WHERE title = 'Общероссийская общественная организация "Ассоциация специалистов в области медицины')),

 ((SELECT id FROM users WHERE email = 'Doctor@stometolog'), (SELECT id FROM educations WHERE title = 'Московский государственный медико-стоматологический университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@stometolog'), (SELECT id FROM educations WHERE title = 'Институт повышения квалификации и переподготовки кадров в здравоохранении')),
 ((SELECT id FROM users WHERE email = 'Doctor@stometolog'), (SELECT id FROM educations WHERE title = 'Российская централизованная медицинская ассоциация (РЦМА)')),

 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM educations WHERE title = 'Челябинская государственная медицинская академия')),
 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM educations WHERE title = 'Федеральный медицинский исследовательский центр имени В. А. Алмазова')),
 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM educations WHERE title = 'Национальный медицинский исследовательский центр терапии и профилактической медицины')),
 ((SELECT id FROM users WHERE email = 'Doctor@psiholog'), (SELECT id FROM educations WHERE title = 'Учебный центр повышения квалификации медицинских работников')),

 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Национальный исследовательский институт общественного здоровья')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Томский государственный университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Научный центр здоровья детей (повышение квалификации педиатров)')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Общероссийская общественная организация "Ассоциация специалистов в области медицины')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Учебный центр повышения квалификации медицинских работников')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin'), (SELECT id FROM educations WHERE title = 'Институт усовершенствования врачей (ИУВ) при МГМУ им. И. М. Сеченова')),

 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM educations WHERE title = 'Орловский государственный университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM educations WHERE title = 'Учебный центр повышения квалификации медицинских работников')),
 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM educations WHERE title = 'Челябинская государственная медицинская академия')),
 ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM educations WHERE title = 'Институт усовершенствования врачей (ИУВ) при МГМУ им. И. М. Сеченова')),

 ((SELECT id FROM users WHERE email = 'Doctor@sun-head-doctor'), (SELECT id FROM educations WHERE title = 'Станция скорой медицинской помощи (курсы повышения квалификации водителей и медиков)')),
 ((SELECT id FROM users WHERE email = 'Doctor@sun-head-doctor'), (SELECT id FROM educations WHERE title = 'Крымский федеральный университет (медицинский факультет)')),

 ((SELECT id FROM users WHERE email = 'Doctor@admin2'), (SELECT id FROM educations WHERE title = 'Ставропольский государственный медицинский университет')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin2'), (SELECT id FROM educations WHERE title = 'Челябинская государственная медицинская академия')),

 ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM educations WHERE title = 'Новосибирский государственный университет (факультет медицины и биомедицинских технологий)')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM educations WHERE title = 'Бурятский государственный университет (медицинский факультет)')),
 ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM educations WHERE title = 'Московский научно-практический центр психиатроии'));


 --changeset MedicineService:insert-default-specialist-photo

 insert into photos (title) values
 ('/temp/medic-1.png'), ('/temp/medic-2.png'), ('/temp/medic-3.png'), ('/temp/medic-4.png'), ('/temp/medic-5.png'),
 ('/temp/medic-6.png'), ('/temp/admin-1.png'), ('/temp/admin-2.png'), ('/temp/admin-3.png'), ('/temp/admin-4.png'),
 ('/temp/admin-5.png');


 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-1.png') where email = 'Doctor@kardiolog';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-2.png') where email = 'Doctor@onkolog';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-3.png') where email = 'Doctor@okulist';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-4.png') where email = 'Doctor@terapevt';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-5.png') where email = 'Doctor@stometolog';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/medic-6.png') where email = 'Doctor@psiholog';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/admin-1.png') where email = 'Doctor@admin';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/admin-2.png') where email = 'Doctor@head-doctor';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/admin-3.png') where email = 'Doctor@sun-head-doctor';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/admin-4.png') where email = 'Doctor@admin2';
 update users set photo_id = (SELECT id FROM photos WHERE title = '/temp/admin-5.png') where email = 'Doctor@admin3';

 --changeset MedicineService:insert-default-specialist-add-roles
 insert into users_roles (user_id, role_id) values
  ((SELECT id FROM users WHERE email = 'Doctor@head-doctor'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
  ((SELECT id FROM users WHERE email = 'Doctor@sun-head-doctor'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
  ((SELECT id FROM users WHERE email = 'Doctor@admin2'), (SELECT id FROM roles WHERE title = 'SPECIALIST')),
  ((SELECT id FROM users WHERE email = 'Doctor@admin3'), (SELECT id FROM roles WHERE title = 'SPECIALIST'));

 --changeset MedicineService:insert-default-specialist-update-position

 update positions set title = 'Врач' where title = ('DOCTOR');
 update positions set title = 'Администратор' where title = ('ADMINISTRATION');
 update positions set title = 'Мл. Администратор' where title = ('SUB_ADMINISTRATION');
 update positions set title = 'Главный врач' where title = ('HEAD_DOCTOR');
 update positions set title = 'Помошник Гл. Врача' where title = ('SUB_HEAD_DOCTOR');


  --changeset MedicineService:insert-default-specialist-update-statuses

  update statuses set title = 'В работе' where title = ('IN_WORK');
  update statuses set title = 'Выходной' where title = ('DAY_OFF');
  update statuses set title = 'Отпуск' where title = ('VACATION');
  update statuses set title = 'В сети' where title = ('ONLINE');
  update statuses set title = 'Не в сети' where title = ('OFFLINE');
