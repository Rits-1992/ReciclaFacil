package com.interdisciplinar.lp2.demo.DTO.mensagem;

import java.time.LocalDateTime;

import com.interdisciplinar.lp2.demo.Entities.StatusMensagem;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensagemResponseDTO {

    private Long id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataEnvio;
    private StatusMensagem status;
    private Long usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
}