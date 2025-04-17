package com.example.api.clientes.domain.repository;

import com.example.api.clientes.domain.model.Endereco;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByCep(String cep);
}