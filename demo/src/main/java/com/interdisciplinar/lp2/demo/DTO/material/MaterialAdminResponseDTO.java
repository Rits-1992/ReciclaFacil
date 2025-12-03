package com.interdisciplinar.lp2.demo.DTO.material;

import java.util.Set;

public record MaterialAdminResponseDTO(
        Long id,
        String nome,
        String descricao,
        boolean reciclavel,
        TipoDescarteResumoDTO tipoDescarte,
        Set<LocalDescarteResumoDTO> locaisDescarte
) {

    public record TipoDescarteResumoDTO(
            Long id,
            String nome
    ) {}

    public record LocalDescarteResumoDTO(
            Long id,
            String nome
    ) {}
}