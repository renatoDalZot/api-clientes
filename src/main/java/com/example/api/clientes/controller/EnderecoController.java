package com.example.api.clientes.controller;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.application.service.EnderecoService;
import com.example.api.clientes.infraestrutura.EnderecoConsulta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;
    private final EnderecoConsulta enderecoConsulta;

    @ConditionalOnProperty (name = "app.endereco.cadastrar.via.json", havingValue = "true", matchIfMissing = true)
    @PostMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> cadastrarJson (@PathVariable @NotEmpty String cpf, @RequestBody @Valid EnderecoRequest enderecoRequest) {
        return enderecoService.cadastrar(cpf, enderecoRequest)
                        .map(body -> ResponseEntity.created(URI.create("/v1/clientes/" + cpf + "/endereco/" + body.id())).body(body))
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ConditionalOnProperty (name = "app.endereco.cadastrar.via.plaintext", havingValue = "true", matchIfMissing = false)
    @PostMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> cadastrarPlainText (@PathVariable @NotEmpty String cpf, @RequestBody @Valid EnderecoRequest enderecoRequest) {
        // TODO: implementar
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> buscarPorCpf(@PathVariable String cpf) {
        return enderecoConsulta.findByCpf(cpf).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
