package com.interdisciplinar.lp2.demo.DTO.mensagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensagemRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A mensagem não pode estar vazia")
    private String conteudo;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
}