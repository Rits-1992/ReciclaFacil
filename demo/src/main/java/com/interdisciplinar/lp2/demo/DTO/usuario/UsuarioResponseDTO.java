package com.interdisciplinar.lp2.demo.DTO.usuario;

import java.time.LocalDateTime;

import com.interdisciplinar.lp2.demo.Entities.TipoUsuario;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    TipoUsuario tipoUsuario,
    LocalDateTime dataCadastro
) {}
