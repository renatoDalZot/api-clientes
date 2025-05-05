package com.example.api.clientes.controller;


import com.example.api.clientes.application.PessoaFisicaApplicationService;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pessoa-fisica")
public class PessoaFisicaController {

    private final PessoaFisicaApplicationService pessoaFisicaApplicationService;

    public PessoaFisicaController(PessoaFisicaApplicationService pessoaFisicaApplicationService) {
        this.pessoaFisicaApplicationService = pessoaFisicaApplicationService;
    }

    @PostMapping
    ResponseEntity<PessoaFisicaResponse> cadastrar(@RequestBody PessoaFisicaRequest pessoaFisicaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaFisicaApplicationService.cadastrar(pessoaFisicaRequest));
    }

    @GetMapping("/{id}")
    ResponseEntity<PessoaFisicaResponse> buscarPorId(@PathVariable Long id) {
        return pessoaFisicaApplicationService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{number}")
    ResponseEntity<PessoaFisicaResponse> buscarPorCpf(@PathVariable String number) {
        return pessoaFisicaApplicationService.buscarPorCpf(number)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
