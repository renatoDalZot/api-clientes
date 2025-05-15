package com.example.api.clientes.application.service;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.repository.EnderecoRepository;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;

    @Transactional
    public Optional<EnderecoResponse> cadastrar (String cpf, EnderecoRequest enderecoRequest)  {
        return pessoaFisicaRepository.findByCpf(cpf).map(pessoaFisica -> {
            var endereco = getEnderecoFromRequestData(enderecoRequest, pessoaFisica.getId());
            var enderecoSalvo = enderecoRepository.save(endereco);
            return toEnderecoDto(enderecoSalvo);
        });
    }

    private static Endereco getEnderecoFromRequestData(EnderecoRequest enderecoRequest, Long pessoaId) {
        return new Endereco(
                pessoaId,
                enderecoRequest.logradouro(),
                enderecoRequest.numero(),
                enderecoRequest.complemento(),
                enderecoRequest.bairro(),
                enderecoRequest.cep(),
                enderecoRequest.cidade(),
                enderecoRequest.estado()
        );
    }

    private EnderecoResponse toEnderecoDto(Endereco enderecoSalvo) {
        return new EnderecoResponse(enderecoSalvo.getId(), enderecoSalvo.getLogradouro(),
                enderecoSalvo.getNumero(), enderecoSalvo.getComplemento(), enderecoSalvo.getBairro(),
                enderecoSalvo.getCep(), enderecoSalvo.getCidade(), enderecoSalvo.getEstado());
    }
}
