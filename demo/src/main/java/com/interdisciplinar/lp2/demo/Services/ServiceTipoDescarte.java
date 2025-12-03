package com.interdisciplinar.lp2.demo.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteResumoDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityTipoDescarte;
import com.interdisciplinar.lp2.demo.Mapper.MapperTipoDescarte;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryTipoDescarte;

@Service
@Transactional
public class ServiceTipoDescarte {

    private final RepositoryTipoDescarte repository;
    private final MapperTipoDescarte mapperTipoDescarte = new MapperTipoDescarte();

    public ServiceTipoDescarte(RepositoryTipoDescarte repository) {
        this.repository = repository;
    }

    /* ============================================================
       LISTAGEM GERAL (ADMIN)
    ============================================================ */
    public List<TipoDescarteAdminResponseDTO> listarTodosAdmin() {
        return repository.findAll()
                .stream()
                .map(mapperTipoDescarte::toAdminDTO)
                .toList();
    }

    /* ============================================================
       LISTAGEM RESUMIDA (USUÁRIO)
    ============================================================ */
    public List<TipoDescarteResumoDTO> listarResumo() {
        return repository.findAll()
                .stream()
                .map(mapperTipoDescarte::toResumoDTO)
                .toList();
    }

    /* ============================================================
       BUSCAR POR ID
    ============================================================ */
    public TipoDescarteDTO buscarPorId(Long id) {
        EntityTipoDescarte tipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de descarte não encontrado"));

        return mapperTipoDescarte.toDTO(tipo);
    }

    /* ============================================================
       LISTAR POR MATERIAL
    ============================================================ */
    public List<TipoDescarteResumoDTO> listarPorMaterial(Long materialId) {
        return repository.findByMaterial_Id(materialId)
                .stream()
                .map(mapperTipoDescarte::toResumoDTO)
                .toList();
    }

}