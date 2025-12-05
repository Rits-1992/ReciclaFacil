package com.interdisciplinar.lp2.demo.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Table(name = "vw_mensagens_completo")
@Immutable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensagensCompletoView {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "conteudo")
    private String conteudo;

    @Column(name = "status")
    private String status;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @Column(name = "email_usuario")
    private String emailUsuario;
}
