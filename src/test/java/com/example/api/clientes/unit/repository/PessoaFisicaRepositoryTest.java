package com.example.api.clientes.unit.repository;

import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
public class PessoaFisicaRepositoryTest {

    @Autowired
    private PessoaFisicaRepository repository;

    @Autowired
    private TestEntityManager entityManager;

   @Test
   void dadoNovoCadastro_quandoSalvoNoRepositorio_entaoOsDadosPersistem() {
       // Cenário
       var pessoaEsperada = new PessoaFisica("João", "12345678900", LocalDate.of(2025,4,5),
               LocalDate.of(2000,1,1));

       // Ação
       repository.save(pessoaEsperada);
       entityManager.flush();
       entityManager.clear();

       // Verificação
       PessoaFisica pessoaAtual = entityManager.find(PessoaFisica.class, pessoaEsperada.getId());
       assertPessoaAtualEqualsPessoaEsperada(pessoaAtual, pessoaEsperada);
   }

    @Test
    void dadoCadastroNoBanco_quandoProcurado_entaoSucesso() {
       //Cenário
        PessoaFisica pessoaEsperada = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025, 4, 5),
                LocalDate.of(2000, 1, 1));
        entityManager.persist(pessoaEsperada);
        entityManager.flush();
        entityManager.clear();

        // Ação e Verificação
        assertThat(repository.findById(pessoaEsperada.getId())).hasValueSatisfying(pessoaAtual -> {
            assertPessoaAtualEqualsPessoaEsperada(pessoaAtual, pessoaEsperada);
        });
    }

    @Test
    void dadoCadastroNoBanco_quandoProcuradoPorCpf_entaoSucesso() {
        // Cenário
        PessoaFisica pessoaEsperada = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));
        entityManager.persist(pessoaEsperada);
        entityManager.flush();
        entityManager.clear();

        // Ação e Verificação
        assertThat(repository.findByCpf("98765432100")).hasValueSatisfying(pessoaAtual -> {
            assertPessoaAtualEqualsPessoaEsperada(pessoaAtual, pessoaEsperada);
        });
    }

    private void assertPessoaAtualEqualsPessoaEsperada(PessoaFisica pessoaAtual, PessoaFisica pessoaEsperada) {
        assertThat(pessoaAtual).isNotNull();
        assertThat(pessoaAtual.getId()).isEqualTo(pessoaEsperada.getId());
        assertThat(pessoaAtual.getNome()).isEqualTo(pessoaEsperada.getNome());
        assertThat(pessoaAtual.getCpf()).isEqualTo(pessoaEsperada.getCpf());
        assertThat(pessoaAtual.getDataCadastro()).isEqualTo(pessoaEsperada.getDataCadastro());
        assertThat(pessoaAtual.getDataNascimento()).isEqualTo(pessoaEsperada.getDataNascimento());
    }

}
