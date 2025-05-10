package com.example.api.clientes.unit.domain;

import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.model.PessoaFisica;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnderecoTest {

    @Test
    void dadoNovoEnderecoQuandoCepInvalidoDeveLancarExcecao() {
        // Cenário e Verificação
        assertThrows(IllegalArgumentException.class, () -> {
            new Endereco(1L,"Rua A", 123, null, "Bairro B","01654",
                    "Cidade C","Estado D");
        });
    }

    @Test
    void dadoNovoEnderecoQuandoCepValidoDeveTerSucesso() {
        // Cenário e Ação
        Endereco endereco = new Endereco(1L,"Rua A", 123, null, "Bairro B","01654-000",
                "Cidade C","Estado D");

        // Verificação
        assert endereco.getCep().equals("01654-000");
        assert endereco.getLogradouro().equals("Rua A");
        assert endereco.getNumero() == 123;
        assert endereco.getBairro().equals("Bairro B");
        assert endereco.getCidade().equals("Cidade C");
        assert endereco.getEstado().equals("Estado D");
        assert endereco.getComplemento() == null;
    }
}
