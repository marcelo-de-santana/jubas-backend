INSERT INTO tb_user (id, email, password, permission) VALUES (0x2EBA17A0F7384107BF5A85BE53EC0FE3,'admin@jubas.com','$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm',0);
INSERT INTO tb_user (id, email, password, permission) VALUES (0xC2FCC2E5D07748BA8604E735D77168EE,'gerente@jubas.com','1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm',1);
INSERT INTO tb_user (id, email, password, permission) VALUES (0xD526F2C160B047198C5DFDB60CE7E72A,'gabriel.navalha@jubas.com','1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm',1);
INSERT INTO tb_user (id, email, password, permission) VALUES (0xDEECD1007DEC4DBDAC0F4211650AE528,'gerson.castro@jubas.com','1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm',1);
INSERT INTO tb_user (id, email, password, permission) VALUES (0xE8488082ACD9436F9AEBC22F9F409133,'oliver.castro@jubas.com','1$2a$10$IDG7gEgIiAwLML0xgUUBUexuJ9nxNitpCZyMJlYn/PjC0XAv3VfMm',1);
INSERT INTO tb_user (id, email, password, permission) VALUES (0xCBF3717AC2CC4DFD93F8B2E6C67AA7F9,'cliente@gmail.com','$2a$10$qYIiCALEL0ns5gGJzS9ryuRiFQrBInbgO9POMn1Ug9G/213DWDmbS',2);

INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (1, '09:00:00', '18:00:00', '13:00:00', '14:00:00');
INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (2, '08:00:00', '19:00:00', '13:00:00', '14:00:00');
INSERT INTO tb_working_hour (id, start_time, end_time, start_interval, end_interval) VALUES (3, '10:00:00', '20:00:00', '13:00:00', '14:00:00');

-- CLIENT
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (0xE8CB8B11508E4E099DBD6FADF015E28C,'Pai do Fulano','00000000000',true,0xCBF3717AC2CC4DFD93F8B2E6C67AA7F9);
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (0x0B8B82F6A8EF474893EAFA3FCBDC8D8B,'Filho de Fulano','00000000001',true,0xCBF3717AC2CC4DFD93F8B2E6C67AA7F9);
-- BARBER
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (0x1253FC4445C5440CB0D81B9A2C5DC919,'Gabriel Navalha',null,true,0xD526F2C160B047198C5DFDB60CE7E72A);
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (0x6603391C551848E2951D41AA0760ECE9,'Gerson de Castro',null,false,0xDEECD1007DEC4DBDAC0F4211650AE528);
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES (0xD3B3D0DB5BB04DAF85C1193403C9F36C,'Oliver Barbeiro','33333333333',false,0xE8488082ACD9436F9AEBC22F9F409133);

INSERT INTO tb_employee (employee_id, working_hour_id) VALUES (0x1253FC4445C5440CB0D81B9A2C5DC919, 1);
INSERT INTO tb_employee (employee_id, working_hour_id) VALUES (0x6603391C551848E2951D41AA0760ECE9, 1);

INSERT INTO tb_category (id, name) VALUES (1,'Corte de cabelo masculino');
INSERT INTO tb_category (id, name) VALUES (2,'Serviços de barba');
INSERT INTO tb_category (id, name) VALUES (3,'Tratamentos capilares');
INSERT INTO tb_category (id, name) VALUES (4,'Depilação');
INSERT INTO tb_category (id, name) VALUES (5,'Cuidados com a pele');
INSERT INTO tb_category (id, name) VALUES (6,'Outros serviços');

INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (0x78be8b22957642c9a8190e217b10fa30,'Corte clássico com tesoura','00:40:00',15.99,1);
INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (0x408d3f22f41d436d8fee2f9dfa23965b,'Corte moderno com máquina','00:20:00',null,1);

INSERT INTO tb_employee_associated_specialty (employee_id, specialty_id)VALUES (0x1253FC4445C5440CB0D81B9A2C5DC919, 0x78be8b22957642c9a8190e217b10fa30)


# INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (null,'Corte degradê com máquina','00:40:00',1)
# INSERT INTO tb_specialty (id, name, time_duration, price, category_id) VALUES (null,'Corte com tesoura e navalha','00:50:00',1)


--  "data": [
--       {
--         "id_servico": "5",
--         "id_categoria": "2",
--         "id_status_servico": "1",
--         "nome_servico": "Barba completa com tesoura e navalha",
--         "preco": null,
--         "duracao": "00:30:00"
--       },
--       {
--         "id_servico": "6",
--         "id_categoria": "2",
--         "id_status_servico": "1",
--         "nome_servico": "Barba com máquina",
--         "preco": null,
--         "duracao": "00:05:00"
--       },
--       {
--         "id_servico": "7",
--         "id_categoria": "2",
--         "id_status_servico": "1",
--         "nome_servico": "Aparar barba",
--         "preco": null,
--         "duracao": "00:10:00"
--       },
--       {
--         "id_servico": "8",
--         "id_categoria": "2",
--         "id_status_servico": "1",
--         "nome_servico": "Modelar barba",
--         "preco": null,
--         "duracao": "00:15:00"
--       },
--       {
--         "id_servico": "9",
--         "id_categoria": "3",
--         "id_status_servico": "1",
--         "nome_servico": "Lavagem de cabelo",
--         "preco": "5",
--         "duracao": "00:10:00"
--       },
--       {
--         "id_servico": "10",
--         "id_categoria": "3",
--         "id_status_servico": "1",
--         "nome_servico": "Hidratação capilar",
--         "preco": null,
--         "duracao": "00:20:00"
--       },
--       {
--         "id_servico": "11",
--         "id_categoria": "3",
--         "id_status_servico": "1",
--         "nome_servico": "Tratamento para queda de cabelo",
--         "preco": null,
--         "duracao": "00:30:00"
--       },
--       {
--         "id_servico": "12",
--         "id_categoria": "3",
--         "id_status_servico": "1",
--         "nome_servico": "Tratamento para cabelos oleosos",
--         "preco": null,
--         "duracao": "00:20:00"
--       },
--       {
--         "id_servico": "13",
--         "id_categoria": "3",
--         "id_status_servico": "1",
--         "nome_servico": "Tratamento para cabelos ressecados",
--         "preco": null,
--         "duracao": "00:30:00"
--       },
--       {
--         "id_servico": "14",
--         "id_categoria": "4",
--         "id_status_servico": "1",
--         "nome_servico": "Depilação facial com cera",
--         "preco": null,
--         "duracao": "00:15:00"
--       },
--       {
--         "id_servico": "15",
--         "id_categoria": "4",
--         "id_status_servico": "1",
--         "nome_servico": "Depilação corporal com cera",
--         "preco": null,
--         "duracao": "01:00:00"
--       },
--       {
--         "id_servico": "16",
--         "id_categoria": "4",
--         "id_status_servico": "1",
--         "nome_servico": "Depilação íntima masculina",
--         "preco": null,
--         "duracao": "01:00:00"
--       },
--       {
--         "id_servico": "17",
--         "id_categoria": "5",
--         "id_status_servico": "1",
--         "nome_servico": "Limpeza facial",
--         "preco": null,
--         "duracao": "00:30:00"
--       },
--       {
--         "id_servico": "18",
--         "id_categoria": "5",
--         "id_status_servico": "1",
--         "nome_servico": "Massagem facial",
--         "preco": null,
--         "duracao": "00:30:00"
--       },
--       {
--         "id_servico": "19",
--         "id_categoria": "6",
--         "id_status_servico": "1",
--         "nome_servico": "Manicure masculina",
--         "preco": null,
--         "duracao": "00:20:00"
--       },
--       {
--         "id_servico": "20",
--         "id_categoria": "6",
--         "id_status_servico": "1",
--         "nome_servico": "Pedicure masculina",
--         "preco": null,
--         "duracao": "00:20:00"
--       },
--       {
--         "id_servico": "21",
--         "id_categoria": "6",
--         "id_status_servico": "1",
--         "nome_servico": "Design de sobrancelha",
--         "preco": null,
--         "duracao": "00:20:00"
--       },
--       {
--         "id_servico": "22",
--         "id_categoria": "6",
--         "id_status_servico": "1",
--         "nome_servico": "Coloração de cabelo",
--         "preco": null,
--         "duracao": "00:45:00"
--       }
