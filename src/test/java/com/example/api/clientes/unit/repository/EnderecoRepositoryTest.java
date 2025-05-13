package com.example.api.clientes.unit.repository;

import com.example.api.clientes.domain.model.Endereco;
import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.EnderecoRepository;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import com.example.api.clientes.helper.EnderecoBuilder;
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
public class EnderecoRepositoryTest {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

   @Test
   void dadoNovoCadastro_quandoSalvoNoRepositorio_entaoOsDadosPersistem() {
       // Cenário e Ação
         var enderecoEsperado = new EnderecoBuilder().build();

       // Ação
       repository.save(enderecoEsperado);
       entityManager.flush();
       entityManager.clear();

       // Verificação
       Endereco enderecoAtual = entityManager.find(Endereco.class, enderecoEsperado.getId());
       assertEnderecoAtualEqualsEnderecoEsperado(enderecoAtual, enderecoEsperado);
   }

    @Test
    void dadoNovoCadastro_quandoProcurado_entaoSucesso() {
       // Cenário
        var endereco = new EnderecoBuilder().build();
        entityManager.persist(endereco);
        entityManager.flush();
        entityManager.clear();

        // Ação e Verificação
        var enderecoEsperado = new EnderecoBuilder().withComplemento("Apto 5454").build();
        assertThat(repository.findById(endereco.getId())).hasValueSatisfying(enderecoAtual ->
                assertEnderecoAtualEqualsEnderecoEsperado(enderecoAtual, endereco));
    }

    private void assertEnderecoAtualEqualsEnderecoEsperado(Endereco enderecoAtual, Endereco enderecoEsperado) {
        assertThat(enderecoAtual).usingRecursiveComparison().isEqualTo(enderecoEsperado);
    }
}
