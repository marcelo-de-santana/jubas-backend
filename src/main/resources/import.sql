INSERT INTO TB_USER_PERMISSION VALUES(1,'ADMIN');
INSERT INTO TB_USER_PERMISSION VALUES(2,'BARBER');
INSERT INTO TB_USER_PERMISSION VALUES(3,'CLIENT');

INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('272d740c-3984-11ee-be56-0242ac120002','admin@jubas.com', '12345678',1);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('2d9f5652-3984-11ee-be56-0242ac120002','gerente@jubas.com','12345678',1);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('db16f0fa-398a-11ee-be56-0242ac120002','cliente@gmail.com','12345678',3);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('5b4ae402-3366-457a-a891-e37f592538d4','gabriel.navalha@jubas.com','12345678',2);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('75ecf217-e0da-4e59-a43f-d095902e30c6','gerson.castro@jubas.com','12345678',2);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('bdbf5d93-6e55-4841-8604-daae1fa85bb4','oliver.castro@jubas.com','12345678',2);

INSERT INTO tb_working_hours(id, start_time, end_time, start_interval, end_interval) VALUES ('1','09:00:00','18:00:00','13:00:00','14:00:00');
INSERT INTO tb_working_hours(id, start_time, end_time, start_interval, end_interval) VALUES ('2','08:00:00','19:00:00','13:00:00','14:00:00');
INSERT INTO tb_working_hours(id, start_time, end_time, start_interval, end_interval) VALUES ('3','10:00:00','20:00:00','13:00:00','14:00:00');
--CLIENT
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES ('b0c1cbde-398c-11ee-be56-0242ac120002','Pai do Fulano','00000000000',true,'db16f0fa-398a-11ee-be56-0242ac120002');
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES ('9a1104e0-398c-11ee-be56-0242ac120002','Filho de Fulano','00000000001',true,'db16f0fa-398a-11ee-be56-0242ac120002');
--BARBER
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES ('b4ec78bd-d72f-4545-b695-0b40ffe7327d','Gabriel Navalha',null,true,'5b4ae402-3366-457a-a891-e37f592538d4');
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES ('ffeb959a-a95a-4a43-8c06-1763c9da8335','Gerson de Castro',null,false,'75ecf217-e0da-4e59-a43f-d095902e30c6');
INSERT INTO tb_profile (id, name, cpf, status_profile, user_id) VALUES ('9ad1ad9f-d052-4a05-8430-497a8ff165b0','Oliver Barbeiro','33333333333',false,'bdbf5d93-6e55-4841-8604-daae1fa85bb4');

INSERT INTO tb_employee (id, profile_id, working_hours_id) VALUES ('08cec954-4bde-11ee-be56-0242ac120002','b4ec78bd-d72f-4545-b695-0b40ffe7327d','1');
INSERT INTO tb_employee (id, profile_id, working_hours_id) VALUES ('36e6efa6-4bde-11ee-be56-0242ac120002','ffeb959a-a95a-4a43-8c06-1763c9da8335','1');