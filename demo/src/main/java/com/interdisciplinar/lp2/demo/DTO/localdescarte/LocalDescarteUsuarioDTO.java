package com.interdisciplinar.lp2.demo.DTO.localdescarte;

import java.time.LocalTime;

import com.interdisciplinar.lp2.demo.DTO.endereco.EnderecoDTO;

public record LocalDescarteUsuarioDTO(
    String nome,
    LocalTime horarioAbertura,
    LocalTime horarioFechamento,
    String contato,
    String email,
    String descricao,
    EnderecoDTO endereco
) {}
