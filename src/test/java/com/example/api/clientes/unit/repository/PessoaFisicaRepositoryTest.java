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
       var pessoaNova = new PessoaFisica("João", "12345678900", LocalDate.of(2025,4,5),
               LocalDate.of(2000,1,1));

       repository.save(pessoaNova);

       entityManager.flush();
       entityManager.clear();

       assertThat(repository.findById(pessoaNova.getId())).hasValueSatisfying(pessoaResgatada -> {
           assertThat(pessoaResgatada.getId()).isEqualTo(pessoaNova.getId());
           assertThat(pessoaResgatada.getNome()).isEqualTo("João");
           assertThat(pessoaResgatada.getCpf()).isEqualTo("12345678900");
           assertThat(pessoaResgatada.getDataCadastro()).isEqualTo(LocalDate.of(2025,4,5));
           assertThat(pessoaResgatada.getDataNascimento()).isEqualTo(LocalDate.of(2000,1,1));
       });
   }

    @Test
    void dadoNovoCadastro_quandoProcurado_entaoSucesso() {
        PessoaFisica pessoaNova = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025, 4, 5),
                LocalDate.of(2000, 1, 1));

        entityManager.persist(pessoaNova);
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.findById(pessoaNova.getId())).hasValueSatisfying(pessoaResgatada ->
                assertThat(pessoaResgatada.getId()).isEqualTo(pessoaNova.getId()));
    }

    @Test
    void dadoNovoCadastro_quandoProcuradoPorCpf_entaoSucesso() {
        PessoaFisica pessoaNova = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));

        entityManager.persist(pessoaNova);

        assertThat(repository.findByCpf("98765432100")).hasValueSatisfying(pessoaResgatada ->
            assertThat(pessoaResgatada.getCpf()).isEqualTo("98765432100"));

  
    }

}
