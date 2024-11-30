BEGIN
INSERT INTO tb_person(birth_date, cpf, email, first_name, last_name, sex)
VALUES ('2024-11-30', '355.137.120-24', 'teste.teste@gmail.com', 'Teste', 'Teste completo', 'MALE')
    INSERT
INTO tb_phone(is_main, phone_number, person_id, type_phone)
VALUES
    (true, '(62)98433-9587', (SELECT person_id FROM tb_person WHERE cpf = '355.137.120-24'), 'RESIDENTIAL')

INSERT INTO tb_user(creation_date, is_primary_access, user_password, fk_person_id, status, type_user, username)
VALUES
    ('2024-11-30', true, '$2a$10$.8m2IE1/iKzHppJf4fguz.jhYJbBII7wyeqYh7ryqmDuf6ZhbdOaW', (SELECT person_id FROM tb_person WHERE cpf = '355.137.120-24'), 'ACTIVE', 'ADMIN', 'teste.teste')
    COMMIT