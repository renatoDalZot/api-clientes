package com.example.api.clientes.controller;

import com.example.api.clientes.application.dto.EnderecoRequest;
import com.example.api.clientes.application.dto.EnderecoResponse;
import com.example.api.clientes.application.service.EnderecoService;
import com.example.api.clientes.infraestrutura.EnderecoConsulta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping
@ConditionalOnProperty (name = "configuracao.endereco.formato", havingValue = "json", matchIfMissing = true)
public class EnderecoControllerJson {

    private final EnderecoService enderecoService;
    private final EnderecoConsulta enderecoConsulta;

    public EnderecoControllerJson(EnderecoService enderecoService, EnderecoConsulta enderecoConsulta) {
        this.enderecoService = enderecoService;
        this.enderecoConsulta = enderecoConsulta;
    }

    @PostMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> cadastrarJson (@PathVariable @NotEmpty String cpf, @RequestBody @Valid EnderecoRequest enderecoRequest) {
        return enderecoService.cadastrar(cpf, enderecoRequest)
                        .map(body -> ResponseEntity.created(URI.create("/v1/clientes/" + cpf + "/endereco/" + body.id())).body(body))
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/v1/clientes/{cpf}/endereco")
    public ResponseEntity<EnderecoResponse> buscarPorCpf(@PathVariable String cpf) {
        return enderecoConsulta.findByCpf(cpf).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
