package com.interdisciplinar.lp2.demo.Mapper;

import org.springframework.stereotype.Component;

import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityUsuario;

@Component
public class MapperUsuario {

    public UsuarioResponseDTO toDTO(EntityUsuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTipoUsuario(),
            usuario.getDataCadastro()
        );
    }
}
