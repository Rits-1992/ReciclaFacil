package com.interdisciplinar.lp2.demo.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensagem")
public class EntityMensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensagem")
    private Long id;

    @NotBlank(message = "O título da mensagem é obrigatório")
    @Column(name = "tituloMensagem", length = 150, nullable = false)
    private String titulo;

    @NotBlank(message = "O conteúdo da mensagem não pode estar vazio")
    @Column(name = "conteudoMensagem", columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "dataEnvio", insertable = false, updatable = false, nullable = false)
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "statusMensagem", length = 20, nullable = false)
    private StatusMensagem status = StatusMensagem.ENVIADA;

    @Column(name = "status_anterior", length = 20)
    private String statusAnterior;

    @Column(name = "data_ultimo_status_change")
    private LocalDateTime dataUltimoStatusChange;

    /* RELACIONAMENTO: mensagem N:1 usuario */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuarioId", nullable = false)
    private EntityUsuario usuario;

}
