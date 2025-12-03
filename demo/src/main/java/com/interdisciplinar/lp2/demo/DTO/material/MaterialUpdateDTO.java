package com.interdisciplinar.lp2.demo.DTO.material;

import java.util.List;

public record MaterialUpdateDTO(
        Long id,
        String nome,
        String descricao,
        boolean reciclavel,
        Long idTipoDescarte,
        List<Long> idLocaisDescarte
) {}
