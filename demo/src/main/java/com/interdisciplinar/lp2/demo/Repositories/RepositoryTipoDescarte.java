package com.interdisciplinar.lp2.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.EntityTipoDescarte;

@Repository
public interface RepositoryTipoDescarte extends JpaRepository<EntityTipoDescarte, Long> {

    Optional<EntityTipoDescarte> findByNome(String nome);

    Optional<EntityTipoDescarte> findAllById(Long id);

    List<EntityTipoDescarte> findByMaterial_Id(Long materialId);

}
