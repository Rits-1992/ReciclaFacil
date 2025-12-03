package com.interdisciplinar.lp2.demo.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "log_pesquisa")
public final class EntityLogPesquisa {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLogPesquisa")
    private Long id;

    @Column(
        name = "dataHora",
        insertable = false,
        updatable = false,
        nullable = false
    )
    private LocalDateTime dataHora; // Será preenchido automaticamente pelo banco de dados

    // Construtores
    public EntityLogPesquisa() {}
    
    public EntityLogPesquisa(EntityUsuario usuario, EntityMaterial material) {
        if (material == null) throw new IllegalArgumentException("Material não pode ser nulo");
        this.usuario = usuario;
        this.material = material;
    }
    
    /*  Relacionamentos
     *      logPesquisa N:1 usuario
     *      logPesquisa N:1 material
     */
    // Relacionamento 1:N com EntityUsuario
    @ManyToOne(fetch = FetchType.LAZY, optional=true)
    @JoinColumn(name = "usuarioId", nullable = true)
    private EntityUsuario usuario;

    // Relacionamento 1:N com EntityMaterial
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materialId", nullable = false)
    private EntityMaterial material; // material pesquisado
}