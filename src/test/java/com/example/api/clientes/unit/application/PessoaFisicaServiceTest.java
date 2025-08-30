package com.example.api.clientes.unit.application;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.application.service.PessoaFisicaService;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import org.assertj.core.api.Assertions;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.example.api.clientes.unit.provider.PessoaFisicaProvider.pessoaFisicaComDatasEspecificas;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PessoaFisicaServiceTest {

    private PessoaFisicaRepository pessoaFisicaRepositoryMock = EasyMock.createMock(PessoaFisicaRepository.class);
    private final Clock testClock = new Clock() {
        @Override
        public ZoneId getZone() {
            return ZoneId.systemDefault();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return null;
        }

        @Override
        public Instant instant() {
            return LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
    };
    private final PessoaFisicaService service = new PessoaFisicaService(pessoaFisicaRepositoryMock, testClock);
    private final PessoaFisica pessoaMock = pessoaFisicaComDatasEspecificas(
            LocalDate.of(2025, 4, 5), LocalDate.of(2000, 1, 1));


    @Test
    void deveCadastrarPessoaFisicaComSucesso() {
        ReflectionTestUtils.setField(pessoaMock, "id", 1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.findByCpf("12345678900")).andReturn(java.util.Optional.empty());
        EasyMock.expect(pessoaFisicaRepositoryMock.save(EasyMock.anyObject(PessoaFisica.class))).andReturn(pessoaMock);
        EasyMock.replay(pessoaFisicaRepositoryMock);
        PessoaFisicaRequest request = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2000, 1, 1));

        PessoaFisicaResponse response = service.cadastrar(request);

        assertEquals(1L, response.id());
        assertEquals("João da Silva", response.nome());
    }

    @Test
    void naoDeveCadastrarPessoaFisicaJaCadastrada() {
        ReflectionTestUtils.setField(pessoaMock, "id", 1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.findByCpf("12345678900")).andReturn(java.util.Optional.of(pessoaMock));
        EasyMock.replay(pessoaFisicaRepositoryMock);
        PessoaFisicaRequest request = new PessoaFisicaRequest("João da Silva", "12345678900",
                LocalDate.of(2000, 1, 1));

        Assertions.assertThatThrownBy(() -> {
            service.cadastrar(request);
        }).isInstanceOf(BusinessException.class).hasMessage("Pessoa física já cadastrada. CPF 12345678900");
    }

    @Test
    void deveBuscarPessoaFisicaPorId() {
        ReflectionTestUtils.setField(pessoaMock, "id", 1L);
        EasyMock.expect(pessoaFisicaRepositoryMock.findById(1L)).andReturn(java.util.Optional.of(pessoaMock));
        EasyMock.replay(pessoaFisicaRepositoryMock);

        var pessoaRecuperada = service.buscarPorId(1L);

        assertEquals("João da Silva", pessoaRecuperada.map(PessoaFisicaResponse::nome).orElse(null));
    }
}
