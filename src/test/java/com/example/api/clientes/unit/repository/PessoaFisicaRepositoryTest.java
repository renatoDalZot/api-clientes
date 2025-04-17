package com.example.api.clientes.unit.repository;

import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
public class PessoaFisicaRepositoryTest {

    @Autowired
    private PessoaFisicaRepository repository;

    @Test
    void deveSalvarPessoaFisica() {
        PessoaFisica pessoaFisica = new PessoaFisica("João", "12345678900", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));

        var pessoaSalva = repository.save(pessoaFisica);
        var pessoaRecuperada = repository.findById(pessoaSalva.getId());

        assert pessoaRecuperada.isPresent();
        assert pessoaRecuperada.get().getNome().equals("João");
    }

    @Test
    void deveRecuperarPessoafisicaPorCpf() {
        PessoaFisica pessoaFisica = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));
        var pessoaSalva = repository.save(pessoaFisica);

        var pessoaRecuperada = repository.findByCpf(pessoaSalva.getCpf());

        assert pessoaRecuperada.isPresent();
        assert pessoaRecuperada.get().getCpf().equals("98765432100");
    }

    @Test
    void deveDeletarPessoaFisica() {
        PessoaFisica pessoaFisica = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));
        var pessoaSalva = repository.save(pessoaFisica);

        repository.deleteById(pessoaSalva.getId());

        assertTrue(repository.findById(pessoaSalva.getId()).isEmpty());
    }




}
