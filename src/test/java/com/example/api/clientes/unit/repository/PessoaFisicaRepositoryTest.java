package com.example.api.clientes.unit.repository;

import com.example.api.clientes.domain.model.PessoaFisica;
import com.example.api.clientes.domain.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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
   void dadoNovoCadastro_quandoSalvoNoRepositorio_entaoSucesso() {
       var pessoaNova = new PessoaFisica("João", "12345678900", LocalDate.of(2025,4,5),
               LocalDate.of(2000,1,1));

       PessoaFisica pessoaSalva = repository.save(pessoaNova);

       assertThat(pessoaSalva.getCpf()).isEqualTo("12345678900");
       assertThat(pessoaSalva.getNome()).isEqualTo("João");
   }

   @Test
   void dadoNovoCadastro_quandoPersistido_entaoDadosPersistidosCorretamente() {
       var pessoaNova = new PessoaFisica("João", "12345678900", LocalDate.of(2025,4,5),
               LocalDate.of(2000,1,1));

       entityManager.persistAndFlush(pessoaNova);

       PessoaFisica pessoaResgatada = (PessoaFisica) entityManager.getEntityManager()
               .createNativeQuery("SELECT * FROM pessoa_fisica WHERE cpf = :cpf", PessoaFisica.class)
               .setParameter("cpf", "12345678900")
               .getSingleResult();

       assertThat(pessoaResgatada.getNome()).isEqualTo("João");
   }

    @Test
    void dadoNovoCadastro_quandoProcurado_entaoSucesso() throws NoSuchFieldException, IllegalAccessException {
        PessoaFisica pessoaNova = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));
        var nomePessoaNova = "Maria";

        entityManager.persist(pessoaNova);
        Field idField = PessoaFisica.class.getDeclaredField("id");
        idField.setAccessible(true);

        Optional<PessoaFisica> pessoaRecuperada = repository.findById((Long) Objects.requireNonNull(idField.get(pessoaNova)));
        var nomePessoaRecuperada = Objects.requireNonNull(pessoaRecuperada.orElse(null)).getNome();

        assertThat(nomePessoaNova).isEqualTo(nomePessoaRecuperada);
    }

    @Test
    void dadoNovoCadastro_quandoProcuradoPorCpf_entaoSucesso() throws NoSuchFieldException, IllegalAccessException {
        PessoaFisica pessoaNova = new PessoaFisica("Maria", "98765432100", LocalDate.of(2025,4,5), LocalDate.of(2000,1,1));
        var nomePessoaNova = "Maria";
        var cpfPessoaNova = "98765432100";
        entityManager.persist(pessoaNova);
        Field cpfField = PessoaFisica.class.getDeclaredField("cpf");
        cpfField.setAccessible(true);

        Optional<PessoaFisica> pessoaRecuperada = repository.findByCpf(cpfPessoaNova);
        var cpfPessoaRecuperada = (String) cpfField.get(pessoaRecuperada.orElse(null));

        assertThat(cpfPessoaNova).isEqualTo(cpfPessoaRecuperada);
    }

}
