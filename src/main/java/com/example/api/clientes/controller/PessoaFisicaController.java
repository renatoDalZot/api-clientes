package com.example.api.clientes.controller;


import com.example.api.clientes.application.service.PessoaFisicaService;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
public class PessoaFisicaController {

    private final PessoaFisicaService pessoaFisicaService;

    @PostMapping
    ResponseEntity<PessoaFisicaResponse> cadastrar(@RequestBody @Valid PessoaFisicaRequest pessoaFisicaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaFisicaService.cadastrar(pessoaFisicaRequest));
    }

    @GetMapping("/{id}")
    ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        return pessoaFisicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{number}")
    ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String number) {
        return pessoaFisicaService.buscarPorCpf(number)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
