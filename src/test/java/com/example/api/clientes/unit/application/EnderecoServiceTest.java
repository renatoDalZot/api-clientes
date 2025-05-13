package com.example.api.clientes.unit.application;

import com.example.api.clientes.application.service.EnderecoService;
import com.example.api.clientes.domain.repository.EnderecoRepository;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import com.example.api.clientes.helper.EnderecoBuilder;
import com.example.api.clientes.helper.PessoaFisicaBuilder;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.easymock.EasyMock.verify;
import static org.mockito.Mockito.times;

public class EnderecoServiceTest {

    private EnderecoRepository enderecoRepository = EasyMock.createMock(EnderecoRepository.class);
    private PessoaFisicaRepository pessoaFisicaRepository = EasyMock.createMock(PessoaFisicaRepository.class);
    private EnderecoService enderecoService = new EnderecoService(enderecoRepository, pessoaFisicaRepository);
    private final EnderecoBuilder enderecoBuilder = new EnderecoBuilder();
    private final PessoaFisicaBuilder pessoaFisicaBuilder = new PessoaFisicaBuilder();

    @Test
    void deveCadastrarEnderecoComSucesso() {
        // Cenário
        EasyMock.expect(pessoaFisicaRepository.findByCpf(EasyMock.anyObject())).andReturn(Optional.of(pessoaFisicaBuilder.build()));
        EasyMock.replay(pessoaFisicaRepository);
        EasyMock.expect(enderecoRepository.save(EasyMock.anyObject())).andReturn(enderecoBuilder.build());
        EasyMock.replay(enderecoRepository);

        // Ação
        var response = enderecoService.cadastrar("12345678900", enderecoBuilder.buidRequest());

        // Verificação
        assert response.isPresent();
        assert response.get().id() != null;
        verify(enderecoRepository);
        verify(pessoaFisicaRepository);
    }

    @Test
    void naoDeveCadastrarEnderecoQuandoCpfNaoForEncontrado() {
        // Cenário
        EasyMock.expect(pessoaFisicaRepository.findByCpf(EasyMock.anyObject())).andReturn(Optional.empty());
        EasyMock.replay(pessoaFisicaRepository);

        // Ação
        var response = enderecoService.cadastrar("12345678900", enderecoBuilder.buidRequest());

        // Verificação
        assert response.isEmpty();
        verify(pessoaFisicaRepository);
    }
}
