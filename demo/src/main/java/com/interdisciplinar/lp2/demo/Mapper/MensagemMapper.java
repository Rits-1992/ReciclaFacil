package com.interdisciplinar.lp2.demo.Mapper;

import org.springframework.stereotype.Component;

import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityMensagem;

@Component
public class MensagemMapper {

    public EntityMensagem toEntity(MensagemRequestDTO dto) {
        EntityMensagem msg = new EntityMensagem();
        msg.setTitulo(dto.getTitulo());
        msg.setConteudo(dto.getConteudo());
        return msg;
    }

    public MensagemResponseDTO toResponse(EntityMensagem entity) {
        return new MensagemResponseDTO(
            entity.getId(),
            entity.getTitulo(),
            entity.getConteudo(),
            entity.getDataEnvio(),
            entity.getStatus(),
            entity.getUsuario().getId(),
            entity.getUsuario().getNome(),
            entity.getUsuario().getEmail()
        );
    }
}
