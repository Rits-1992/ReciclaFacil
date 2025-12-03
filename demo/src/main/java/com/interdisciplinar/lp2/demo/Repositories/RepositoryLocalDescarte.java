package com.interdisciplinar.lp2.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.EntityLocalDescarte;

@Repository
public interface RepositoryLocalDescarte extends JpaRepository<EntityLocalDescarte, Long> {

    // Buscar por nome contendo (para admins)
    List<EntityLocalDescarte> findByNomeContainingIgnoreCase(String nome);

    // Buscar ativos (para usuários comuns)
    List<EntityLocalDescarte> findBySituacaoTrue();

    // Buscar ativos pelo nome (para usuários filtrarem)
    List<EntityLocalDescarte> findBySituacaoTrueAndNomeContainingIgnoreCase(String nome);

    // Buscar locais que aceitam um tipo de material específico
    // (útil quando o usuário pesquisa um material)
    List<EntityLocalDescarte> findByMateriais_Id(Long idMaterial);

    // Buscar locais ativos que aceitam um tipo de material
    List<EntityLocalDescarte> findByMateriais_IdAndSituacaoTrue(Long idMaterial);

    // Locais que aceitam determinado tipo de descarte
    List<EntityLocalDescarte> findByTiposDescarte_Id(Long tipoId);
}
