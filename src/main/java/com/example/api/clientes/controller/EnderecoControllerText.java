package com.example.api.clientes.controller;


import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.application.service.EnderecoService;
import com.example.api.clientes.infraestrutura.EnderecoConsulta;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "configuracao.endereco.formato", havingValue = "text", matchIfMissing = false)
public class EnderecoControllerText {

    private final EnderecoService enderecoService;
    private final EnderecoConsulta enderecoConsulta;

    @PostMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<String> cadastrarPlainText(@PathVariable @NotEmpty String cpf, @RequestBody String plainTextEndereco) {
        return enderecoService.cadastrarPlainText(cpf, plainTextEndereco)
                .map(body -> ResponseEntity.created(URI.create("/v1/clientes/" + cpf + "/endereco/" + body.id())).body(formatEnderecoPlainText(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<String> buscarPorCpf(@PathVariable String cpf) {
        return enderecoConsulta.findByCpf(cpf)
                .map(body -> ResponseEntity.ok(formatEnderecoPlainText(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static String formatEnderecoPlainText(EnderecoResponse body) {
        StringBuilder sb = new StringBuilder();
        sb.append(body.logradouro());
        if (body.numero() != null && !body.numero().isEmpty()) {
            sb.append(", ").append(body.numero());
        }
        if (body.complemento() != null && !body.complemento().isEmpty()) {
            sb.append(" - ").append(body.complemento());
        }
        sb.append(System.lineSeparator());
        sb.append(body.bairro()).append(System.lineSeparator());
        sb.append(body.cep()).append(System.lineSeparator());
        sb.append(body.cidade()).append(" / ").append(body.estado());
        return sb.toString();
    }
}
