INSERT INTO pessoa_fisica
    (id, nome, cpf, data_nascimento, data_cadastro, renda_mensal, score)
        VALUES
        (pessoa_fisica_seq.NEXTVAL, 'João', '12345678900', TO_DATE('2000-01-01', 'YYYY-MM-DD'), TO_DATE('2025-01-01', 'YYYY-MM-DD'), 5000.00, 8.5);