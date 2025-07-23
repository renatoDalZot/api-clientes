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

    @PostMapping("/v1/clientes/{cpf}/endereco") // TODO: deverá ter a mesma rota do método anterior, assim que consertarmos o ConditionalOnProperty
    public ResponseEntity<EnderecoResponse> cadastrarPlainText(@PathVariable @NotEmpty String cpf, @RequestBody String plainTextEndereco) {
        return enderecoService.cadastrarPlainText(cpf, plainTextEndereco)
                .map(body -> ResponseEntity.created(URI.create("/v1/clientes/" + cpf + "/endereco/" + body.id())).body(body))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> buscarPorCpf(@PathVariable String cpf) {
        return enderecoConsulta.findByCpf(cpf).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
