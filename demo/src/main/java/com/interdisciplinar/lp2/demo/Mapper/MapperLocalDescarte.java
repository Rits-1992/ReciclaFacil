package com.interdisciplinar.lp2.demo.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityEndereco;
import com.interdisciplinar.lp2.demo.Entities.EntityLocalDescarte;
import com.interdisciplinar.lp2.demo.Entities.EntityTipoDescarte;

@Component
public class MapperLocalDescarte {

    /* ============================================================
       CONVERTE ENTITY → LocalDescarteAdminResponseDTO
    ============================================================ */
    public LocalDescarteAdminResponseDTO toAdminDTO(EntityLocalDescarte local) {
        return new LocalDescarteAdminResponseDTO(
                local.getId(),
                local.getNome(),
                local.getHorarioAbertura(),
                local.getHorarioFechamento(),
                local.getContato(),
                local.getEmail(),
                local.getDescricao(),
                local.isSituacao(),
                toEnderecoDTO(local.getEndereco()),
                toTipoDescarteDTOSet(local.getTiposDescarte())
        );
    }

    /* ============================================================
       CONVERTE Endereço → EnderecoDTO
    ============================================================ */
    private LocalDescarteAdminResponseDTO.EnderecoDTO toEnderecoDTO(EntityEndereco e) {
        if (e == null) {
            return null;
        }
        return new LocalDescarteAdminResponseDTO.EnderecoDTO(
                e.getRua(),
                e.getNumero(),
                e.getComplemento(),
                e.getBairro(),
                e.getCidade(),
                e.getEstado(),
                e.getCep()
        );
    }

    /* ============================================================
       CONVERTE SET<EntityTipoDescarte> → SET<TipoDescarteDTO>
    ============================================================ */
    private Set<LocalDescarteAdminResponseDTO.TipoDescarteDTO> toTipoDescarteDTOSet(
            Set<EntityTipoDescarte> tipos) {

        if (tipos == null) {
            return Set.of();
        }

        return tipos.stream()
                .map(t -> new LocalDescarteAdminResponseDTO.TipoDescarteDTO(
                        t.getId(),
                        t.getNome()
                ))
                .collect(Collectors.toSet());
    }
}
