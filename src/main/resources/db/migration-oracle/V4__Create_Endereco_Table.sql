--V4__Create_Endereco_Table.sql
CREATE TABLE endereco (
    id NUMBER(20,0) PRIMARY KEY,
    logradouro VARCHAR2(255) NOT NULL,
    numero NUMBER(20,0),
    complemento VARCHAR2(255),
    bairro VARCHAR2(255) NOT NULL,
    cep VARCHAR2(255) NOT NULL,
    cidade VARCHAR2(255) NOT NULL,
    estado VARCHAR2(255) NOT NULL
);