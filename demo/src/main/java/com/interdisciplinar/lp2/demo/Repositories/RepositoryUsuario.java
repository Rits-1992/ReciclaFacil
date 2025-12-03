package com.interdisciplinar.lp2.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.EntityUsuario;

@Repository
public interface RepositoryUsuario extends JpaRepository<EntityUsuario, Long> {
    
    // Buscar usuário por email
    Optional<EntityUsuario> findByEmail(String email);
    // Verificar se email já existe
    boolean existsByEmail(String email);
    
    // Buscar usuário por nome
    Optional<EntityUsuario> findByNome(String nome);
    
}