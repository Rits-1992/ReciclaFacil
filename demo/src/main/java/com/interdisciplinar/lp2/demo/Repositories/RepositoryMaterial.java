package com.interdisciplinar.lp2.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.EntityMaterial;

@Repository
public interface RepositoryMaterial extends JpaRepository<EntityMaterial, Long> {

    // Busca para usuários (retorna lista pode ter vários com nomes parecidos)
    List<EntityMaterial> findByNomeContainingIgnoreCase(String nome);

    // Busca exata (útil para validações em admin)
    Optional<EntityMaterial> findByNomeIgnoreCase(String nome);
}
