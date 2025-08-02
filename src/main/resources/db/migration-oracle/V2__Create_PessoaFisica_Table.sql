--V2__Create_PessoaFisica_Table.sql
CREATE TABLE pessoa_fisica (
    id NUMBER(20,0) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(18) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    data_cadastro DATE NOT NULL,
    renda_mensal NUMBER(19, 2),
    score NUMBER(3,1) NOT NULL,
    endereco_id NUMBER(20,0)
);