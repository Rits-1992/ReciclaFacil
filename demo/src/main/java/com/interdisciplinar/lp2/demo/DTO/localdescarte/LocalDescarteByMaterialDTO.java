package com.interdisciplinar.lp2.demo.DTO.localdescarte;

import java.time.LocalTime;

public record LocalDescarteByMaterialDTO(
        String nome,
        LocalTime horarioAbertura,
        LocalTime horarioFechamento,
        String contato,
        String descricao,
        String endereco
) {}
