package com.example.api.clientes.application.service;

import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PessoaFisicaService {

    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final Clock clock;

    @Transactional
    public PessoaFisicaResponse cadastrar(PessoaFisicaRequest pessoaFisicaRequest) {
        PessoaFisica pessoaFisica = new PessoaFisica(
                pessoaFisicaRequest.nome(),
                pessoaFisicaRequest.cpf(),
                LocalDate.now(clock),
                pessoaFisicaRequest.dataNascimento()
        );
        return pessoaToDTO(pessoaFisicaRepository.save(pessoaFisica));
    }

    public Optional<PessoaFisicaResponse> buscarPorId(Long id) {
        return pessoaFisicaRepository.findById(id).map(this::pessoaToDTO);

    }

    public Optional<PessoaFisicaResponse> buscarPorCpf(String number) {
        return pessoaFisicaRepository.findByCpf(number).map(this::pessoaToDTO);
    }

    private PessoaFisicaResponse pessoaToDTO (PessoaFisica pessoa) {
        return new PessoaFisicaResponse(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getDataCadastro(),
                pessoa.getDataNascimento(),
                pessoa.getRendaMensal(),
                pessoa.getScore()
        );
    }
}
