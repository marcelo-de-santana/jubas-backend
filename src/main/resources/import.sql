INSERT INTO TB_USER_PERMISSION VALUES(1,'ADMIN');
INSERT INTO TB_USER_PERMISSION VALUES(2,'BARBER');
INSERT INTO TB_USER_PERMISSION VALUES(3,'USER');

INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('272d740c-3984-11ee-be56-0242ac120002','admin@jubas.com', '12345678',1);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('2d9f5652-3984-11ee-be56-0242ac120002','gerente@jubas.com','12345678',1);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('db16f0fa-398a-11ee-be56-0242ac120002','cliente@gmail.com','12345678',1);
INSERT INTO tb_user (id, email, password, user_permission_id) VALUES ('5b4ae402-3366-457a-a891-e37f592538d4','gabriel.navalha@jubas.com','12345678',2);


INSERT INTO tb_client (id, cpf, name, status_profile, user_id) VALUES ('b0c1cbde-398c-11ee-be56-0242ac120002','00000000000','Pai do Fulano',true,'db16f0fa-398a-11ee-be56-0242ac120002');
INSERT INTO tb_client (id, cpf, name, status_profile, user_id) VALUES ('9a1104e0-398c-11ee-be56-0242ac120002','00000000001','Filho de Fulano',true,'db16f0fa-398a-11ee-be56-0242ac120002');

INSERT INTO tb_barber (id, name, status_profile, id_user, id_operation_time)VALUES ('b4ec78bd-d72f-4545-b695-0b40ffe7327d','Gabriel Navalha',false,'5b4ae402-3366-457a-a891-e37f592538d4', NULL);
