package com.interdisciplinar.lp2.demo.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "historico_mensagem")
public class EntityHistoricoMensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico")
    private Long id;

    @Column(name = "id_mensagem")
    private Long idMensagem;

    @Column(name = "status_anterior")
    private String statusAnterior;

    @Column(name = "status_novo")
    private String statusNovo;

    @Column(name = "data_mudanca")
    private LocalDateTime dataMudanca;
}
