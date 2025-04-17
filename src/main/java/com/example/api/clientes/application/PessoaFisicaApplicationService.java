package com.example.api.clientes.application;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PessoaFisicaApplicationService {

    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public PessoaFisicaResponse cadastrar(PessoaFisicaRequest pessoaFisicaRequest) {
        PessoaFisica pessoaFisica = new PessoaFisica(pessoaFisicaRequest.nome(), pessoaFisicaRequest.cpf(),
                pessoaFisicaRequest.dataCadastro(), pessoaFisicaRequest.dataNascimento());
        return pessoaFisicaRepository.save(pessoaFisica).toResponse();
    }

    public PessoaFisicaResponse buscarPorId(Long id) {
        return pessoaFisicaRepository.findById(id)
                .map(PessoaFisica::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada para o ID: " + id));
    }

    public PessoaFisicaResponse buscarPorCpf(String number) {
        return pessoaFisicaRepository.findByCpf(number)
                .map(PessoaFisica::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa Física não encontrada para o CPF: " + number));
    }
}
