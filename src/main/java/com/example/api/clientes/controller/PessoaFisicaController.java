package com.example.api.clientes.controller;


import com.example.api.clientes.application.PessoaFisicaApplicationService;
import com.example.api.clientes.application.dto.PessoaFisicaRequest;
import com.example.api.clientes.application.dto.PessoaFisicaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
