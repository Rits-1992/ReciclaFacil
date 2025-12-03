package com.interdisciplinar.lp2.demo.DTO.tipodescarte;

import java.util.Set;

public record TipoDescarteDTO(
        Long id,
        String nome,
        Set<MaterialDTO> material
) {

    public record MaterialDTO(
            Long id,
            String nome,
            boolean reciclavel
    ) {}
}
