package com.interdisciplinar.lp2.demo.DTO.localdescarte;

import java.time.LocalTime;
import java.util.Set;

public record LocalDescarteAdminResponseDTO(

    Long id,
    String nome,
    LocalTime horarioAbertura,
    LocalTime horarioFechamento,
    String contato,
    String email,
    String descricao,
    boolean situacao,
    EnderecoDTO endereco,
    Set<TipoDescarteDTO> tiposDescarte

) {

    public record EnderecoDTO(
        String rua,
        Integer numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
    ) {}

    public record TipoDescarteDTO(
        Long id,
        String nome
    ) {}
}