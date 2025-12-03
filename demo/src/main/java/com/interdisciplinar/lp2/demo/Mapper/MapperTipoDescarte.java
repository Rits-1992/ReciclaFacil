package com.interdisciplinar.lp2.demo.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteResumoDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityTipoDescarte;

@Component
public class MapperTipoDescarte {

public TipoDescarteDTO toDTO(EntityTipoDescarte tipo) {
    Set<TipoDescarteDTO.MaterialDTO> materiais = tipo.getMaterial() != null
        ? tipo.getMaterial().stream()
            .map(m -> new TipoDescarteDTO.MaterialDTO(
                m.getId(),
                m.getNome(),
                m.isReciclavel()
            ))
            .collect(Collectors.toSet())
        : Set.of();

    return new TipoDescarteDTO(
        tipo.getId(),
        tipo.getNome(),
        materiais
    );
}

    public TipoDescarteResumoDTO toResumoDTO(EntityTipoDescarte tipo) {
        return new TipoDescarteResumoDTO(
            tipo.getId(),
            tipo.getNome()
        );
    }

    public TipoDescarteAdminResponseDTO toAdminDTO(EntityTipoDescarte tipo) {
        Set<TipoDescarteAdminResponseDTO.MaterialDTO> materiais = tipo.getMaterial().stream()
            .map(m -> new TipoDescarteAdminResponseDTO.MaterialDTO(
                m.getId(),
                m.getNome(),
                m.isReciclavel()
            ))
            .collect(Collectors.toSet());

        return new TipoDescarteAdminResponseDTO(
            tipo.getId(),
            tipo.getNome(),
            materiais
        );
    }
}
