package com.example.api.clientes.application.service;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.application.exception.BusinessException;
import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.repository.EnderecoRepository;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import com.example.api.clientes.helper.EnderecoParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final String separadorMunicipioUf;

    public EnderecoService(EnderecoRepository enderecoRepository, PessoaFisicaRepository pessoaFisicaRepository,
                           @Value("${configuracao.endereco.separador-municipio-uf:/}")String separadorMunicipioUf) {
        this.enderecoRepository = enderecoRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.separadorMunicipioUf = separadorMunicipioUf;
    }

    @Transactional
    public Optional<EnderecoResponse> cadastrar (String cpf, EnderecoRequest enderecoRequest)  {
        return pessoaFisicaRepository.findByCpf(cpf).map(pessoaFisica -> {
            var endereco = getEnderecoFromRequestData(enderecoRequest, pessoaFisica.getId());
            var enderecoSalvo = enderecoRepository.save(endereco);
            return toEnderecoDto(enderecoSalvo);
        });
    }

    @Transactional
    public Optional<EnderecoResponse> cadastrarPlainText (String cpf, String enderecoPlainText) {
        return pessoaFisicaRepository.findByCpf(cpf).map(pessoaFisica -> {
            var endereco = EnderecoParser.getEnderecoFromPlainText(enderecoPlainText, pessoaFisica.getId(), separadorMunicipioUf);
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
