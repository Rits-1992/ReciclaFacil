package com.interdisciplinar.lp2.demo.DTO.material;

import java.util.Set;

public record MaterialPublicResponseDTO(
        String nome,
        boolean reciclavel,
        String descricao,
        String tipoDescarte,
        Set<String> locaisDescarte
) {}
