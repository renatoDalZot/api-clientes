package com.example.api.clientes.unit.application;

import com.example.api.clientes.application.PessoaFisicaApplicationService;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.api.clientes.unit.util.GeradorEntidadesTeste.pessoaFisicaComDatasEspecificas;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PessoaFisicaApplicationServiceTest {

    private PessoaFisicaRepository pessoaFisicaRepositoryMock = EasyMock.createMock(PessoaFisicaRepository.class);
    private PessoaFisicaApplicationService service = new PessoaFisicaApplicationService(pessoaFisicaRepositoryMock);
    private final PessoaFisica pessoaMock = pessoaFisicaComDatasEspecificas(
            LocalDate.of(2025, 4, 5), LocalDate.of(2000, 1, 1));


    @Test
    void deveCadastrarPessoaFisicaComSucesso() {
        pessoaMock.setId(1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.save(EasyMock.anyObject(PessoaFisica.class))).andReturn(pessoaMock);
        EasyMock.replay(pessoaFisicaRepositoryMock);
        PessoaFisicaRequest request = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2025, 4, 5), LocalDate.of(2000, 1, 1));

        PessoaFisicaResponse response = service.cadastrar(request);

        assertEquals(1L, response.id());
        assertEquals("João da Silva", response.nome());
    }

    @Test
    void deveBuscarPessoaFisicaPorId() {
        pessoaMock.setId(1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.findById(1L)).andReturn(java.util.Optional.of(pessoaMock));
        EasyMock.replay(pessoaFisicaRepositoryMock);

        var pessoaRecuperada = service.buscarPorId(1L);

        assertEquals("João da Silva", pessoaRecuperada.nome());
    }

    @Test
    void deveBuscarPessoaFisicaPorCpf() {
        pessoaMock.setId(1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.findByCpf("12345678900")).andReturn(java.util.Optional.of(pessoaMock));
        EasyMock.replay(pessoaFisicaRepositoryMock);

        var pessoaRecuperada = service.buscarPorCpf("12345678900");

        assertEquals("João da Silva", pessoaRecuperada.nome());
    }

}
