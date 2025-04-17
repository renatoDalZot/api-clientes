package com.example.api.clientes.application;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.EnderecoRepository;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnderecoApplicationService {

    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public PessoaFisicaResponse cadastrarEnderecoPessoaFisica(EnderecoRequest enderecoRequest) {
        PessoaFisica pessoaFisica = pessoaFisicaRepository.findById(enderecoRequest.pessoaFisicaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada para o ID: " + enderecoRequest.pessoaFisicaId()));
        if (pessoaFisica.getEndereco() != null) {
            throw new IllegalArgumentException("Pessoa Física já possui um endereço cadastrado.");
        }

        Endereco endereco = new Endereco(enderecoRequest.pessoaFisicaId(), enderecoRequest.logradouro(), enderecoRequest.numero(), enderecoRequest.complemento(),
                enderecoRequest.bairro(), enderecoRequest.cep(), enderecoRequest.municipio(), enderecoRequest.uf());
        enderecoRepository.save(endereco).toResponse();

        return pessoaFisicaRepository.save(pessoaFisica).toResponse();
    }

    public EnderecoResponse buscarPorId(Long id) {
        return enderecoRepository.findById(id)
                .map(Endereco::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para o ID: " + id));
    }
}
