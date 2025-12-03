package com.interdisciplinar.lp2.demo.DTO.localdescarte;

import java.time.LocalTime;

public record LocalDescartePublicResponseDTO(

    String nome,
    LocalTime horarioAbertura,
    LocalTime horarioFechamento,
    String contato,
    String email,
    String descricao,
    EnderecoDTO endereco

) {

    public record EnderecoDTO(
        String rua,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep
    ) {}
}
