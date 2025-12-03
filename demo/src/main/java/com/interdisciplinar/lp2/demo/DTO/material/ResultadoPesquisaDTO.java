package com.interdisciplinar.lp2.demo.DTO.material;

import com.interdisciplinar.lp2.demo.Entities.EntityMaterial;

public record ResultadoPesquisaDTO(
    Long id,
    String nome,
    String descricao,
    String tipoDescarte
) {
    public ResultadoPesquisaDTO(EntityMaterial material) {
        this(
            material.getId(),
            material.getNome(),
            material.getDescricao(),
            material.getTipoDescarte() != null ? material.getTipoDescarte().getNome() : null
        );
    }
}