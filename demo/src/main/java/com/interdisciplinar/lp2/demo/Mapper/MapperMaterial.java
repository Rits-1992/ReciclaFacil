package com.interdisciplinar.lp2.demo.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.interdisciplinar.lp2.demo.DTO.material.MaterialAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialPublicResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityMaterial;

@Component
public final class MapperMaterial {

    public MaterialAdminResponseDTO toAdminDTO(EntityMaterial mat) {
        return new MaterialAdminResponseDTO(
                mat.getId(),
                mat.getNome(),
                mat.getDescricao(),
                mat.isReciclavel(),
                new MaterialAdminResponseDTO.TipoDescarteResumoDTO(
                    mat.getTipoDescarte().getId(),
                    mat.getTipoDescarte().getNome()
                ),
                mat.getLocaisDescarte().stream()
                    .map(l -> new MaterialAdminResponseDTO.LocalDescarteResumoDTO(
                        l.getId(),
                        l.getNome()
                    ))
                    .collect(Collectors.toSet())
        );
    }

    public MaterialPublicResponseDTO toPublicDTO(EntityMaterial mat) {
        return new MaterialPublicResponseDTO(
                mat.getNome(),
                mat.isReciclavel(),
                mat.getDescricao(),
                mat.getTipoDescarte().getNome(),
                mat.getLocaisDescarte().stream()
                        .filter(ld -> ld.isSituacao())
                        .map(l -> l.getNome())
                        .collect(Collectors.toSet())
        );
    }
}