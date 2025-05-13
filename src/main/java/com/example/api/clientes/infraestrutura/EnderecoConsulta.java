package com.example.api.clientes.infraestrutura;

import com.example.api.clientes.application.dto.EnderecoResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EnderecoConsulta {

    private final EntityManager entityManager;

    public Optional<EnderecoResponse> findByCpf(String cpf) {
        try {
            var query = entityManager.createQuery("SELECT new com.example.api.clientes.application.dto.EnderecoResponse (" +
                    "e.id, e.logradouro, e.numero, e.complemento, e.bairro, e.cep, e.cidade, e.estado) " +
                    "FROM Endereco e " +
                    "WHERE e.id = (SELECT p.id FROM PessoaFisica p WHERE p.cpf = :cpf)", EnderecoResponse.class);
            query.setParameter("cpf", cpf);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
