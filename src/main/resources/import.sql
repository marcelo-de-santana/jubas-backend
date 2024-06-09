-- USUÁRIOS
INSERT INTO tb_user (id, email, password, permission) VALUES (UNHEX('550e8400e29b41d4a716446655440000'), 'jubas.barbeiro@gmail.com', '$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm', 0);
INSERT INTO tb_user (id, email, password, permission) VALUES (UNHEX('550e8400e29b41d4a716446655440001'), 'gerente@jubas.com', '1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm', 1);
INSERT INTO tb_user (id, email, password, permission) VALUES (UNHEX('550e8400e29b41d4a716446655440002'), 'gabriel.navalha@jubas.com', '1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm', 1);
INSERT INTO tb_user (id, email, password, permission) VALUES (UNHEX('550e8400e29b41d4a716446655440003'), 'gerson.castro@jubas.com', '1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm', 1);
INSERT INTO tb_user (id, email, password, permission) VALUES (UNHEX('e2fcfda0e75f11eabbc8628eb8d80c33'), 'oliver.castro@jubas.com', '1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm', 1);

-- JORNADAS DE TRABALHO
INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (1, '09:00:00', '18:00:00', '13:00:00', '14:00:00');
INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (2, '08:00:00', '19:00:00', '13:00:00', '14:00:00');
INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (3, '10:00:00', '20:00:00', '13:00:00', '14:00:00');

-- CLIENTES

-- BARBEIROS
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (UNHEX('e2fd01f0e75f11eabbc8628eb8d80c33'), 'Gabriel Navalha', null, true, UNHEX('550e8400e29b41d4a716446655440002'));
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (UNHEX('e2fd0294e75f11eabbc8628eb8d80c33'), 'Gerson de Castro', null, false, UNHEX('550e8400e29b41d4a716446655440003'));
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (UNHEX('e2fd0a54e75f11eabbc8628eb8d80c33'), 'Oliver Barbeiro', '33333333333', false, UNHEX('e2fcfda0e75f11eabbc8628eb8d80c33'));

-- FUNCIONÁRIOS
INSERT INTO tb_employee (employee_id, working_hour_id) VALUES (UNHEX('e2fd01f0e75f11eabbc8628eb8d80c33'), 1);
INSERT INTO tb_employee (employee_id, working_hour_id) VALUES (UNHEX('e2fd0294e75f11eabbc8628eb8d80c33'), 1);

-- CATEGORIAS
INSERT INTO tb_category (id, name) VALUES (1,'Corte de cabelo masculino');
INSERT INTO tb_category (id, name) VALUES (2,'Serviços de barba');
INSERT INTO tb_category (id, name) VALUES (3,'Tratamentos capilares');
INSERT INTO tb_category (id, name) VALUES (4,'Depilação');
INSERT INTO tb_category (id, name) VALUES (5,'Cuidados com a pele');
INSERT INTO tb_category (id, name) VALUES (6,'Outros serviços');

-- ESPECIALIDADES
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('b1a8b85e7ac14a1db7d52e5b88a8d7cd'), 'Corte clássico com tesoura', '00:40:00', 15.99, 1);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('2e2d3d4c6b8f42b19e7c3f1f9f0c8b7e'), 'Corte moderno com máquina', '00:20:00', 9.99, 1);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('c5e8d6f7e9a54d0f98f1b5d7e1c6b4a2'), 'Corte degradê com máquina', '00:40:00', 29.99, 1);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d2f7c9e6b4a54d8b9c2f1e8d9a7b5e3f'), 'Corte com tesoura e navalha', '00:50:00', 37.99, 1);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('e7c9d8f6b3a5c8d9b4f2a3e7b5c8d9e7'), 'Barba completa com tesoura e navalha', '00:30:00', 19.99, 2);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('f1b6c7d9e3a5b2d4c5e6a7b8c9d0e1f2'), 'Barba com máquina', '00:05:00', 4.99, 2);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('e4c7d9b5a2e6f7d3b8a4c9f1e5d7c8b9'), 'Aparar barba', '00:10:00', 4.99, 2);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d9e7f6b3c5a4b2e9d3f5a7c8b4d2e6c7'), 'Modelar barba', '00:15:00', 9.99, 2);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('c8d7e6b5a4c3f2d9e8b1f7a2c5d4e3f6'), 'Lavagem de cabelo', '00:10:00', 4.99, 3);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('a9b8d7e5c4a2f3b1e6d5c7a4e3d2f9b8'), 'Hidratação capilar', '00:20:00', 9.99, 3);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('b7c9d8f6a3b5e4c7d2f1a9e6d7c8b4f2'), 'Tratamento para queda de cabelo', '00:30:00', 19.99, 3);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d8e9c7b5a4f3e6d1b9a7c5d2f8e4b3c6'), 'Tratamento para cabelos oleosos', '00:20:00', 9.99, 3);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('c7e8d6a5b3c9d4f1b2a8e7f5d3c6b4e9'), 'Tratamento para cabelos ressecados', '00:30:00', 19.99, 3);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d3f4b5e6c7a9b8d2e5c4f7a6b3d8e9c1'), 'Depilação facial com cera', '00:15:00', 9.99, 4);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('e9b7c8d6a3f5b2c4d7e1a9b8f6c5d4e2'), 'Depilação corporal com cera', '01:00:00', 39.99, 4);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('c6b5e7d9a4f3c2d1b8e9f5d3a2c7b4e8'), 'Depilação íntima masculina', '01:00:00', 39.99, 4);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d1e2f3c4a5b6e7d8c9b7a6f4e3d2c1b8'), 'Limpeza facial', '00:30:00', 19.99, 5);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('b2a7c9d8e5f3d1b6a4e7f5c3b8d2c6e9'), 'Massagem facial', '00:30:00', 19.99, 5);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('a5b6c7d8e9f3d4c2a7b8c5d1e6f4b9e2'), 'Manicure masculina', '00:20:00', 9.99, 6);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('d2c3b4e5f6a7b8c9d4e1a5b6c3f2e9b8'), 'Pedicure masculina', '00:20:00', 9.99, 6);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('e5a6b7c8d9f4e3c1b2d8a5c6e7b4d3f2'), 'Design de sobrancelha', '00:20:00', 9.99, 6);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (UNHEX('b9c8d7e6a5f4d3c2b1e8a6c5d7f4b2e9'), 'Coloração de cabelo', '00:45:00', 29.99, 6);

INSERT INTO tb_employee_associated_specialty (employee_id, specialty_id) VALUES (UNHEX('e2fd01f0e75f11eabbc8628eb8d80c33'), UNHEX('b1a8b85e7ac14a1db7d52e5b88a8d7cd'));
INSERT INTO tb_employee_associated_specialty (employee_id, specialty_id) VALUES (UNHEX('e2fd01f0e75f11eabbc8628eb8d80c33'), UNHEX('2e2d3d4c6b8f42b19e7c3f1f9f0c8b7e'));
INSERT INTO tb_employee_associated_specialty (employee_id, specialty_id) VALUES (UNHEX('e2fd01f0e75f11eabbc8628eb8d80c33'), UNHEX('c5e8d6f7e9a54d0f98f1b5d7e1c6b4a2'));

# INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (null,'Corte degradê com máquina','00:40:00',1)
# INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (null,'Corte com tesoura e navalha','00:50:00',1)
