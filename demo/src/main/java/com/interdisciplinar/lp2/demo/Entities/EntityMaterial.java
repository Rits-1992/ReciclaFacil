package com.interdisciplinar.lp2.demo.Entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "material")
public class EntityMaterial {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMaterial")
    private Long id;

    @NotBlank(message = "O nome do material não pode ser vazio")
    @Column(name = "nomeMaterial", length = 150, nullable = false)
    private String nome;

    @Column(name = "descricaoMaterial", length = 500)
    private String descricao;

    @Column(name = "reciclavel", nullable = false)
    private boolean reciclavel; // 0 - não, 1 - sim


    /*  Relacionamentos
     *      material N:N localDescarte
     *      material 1:N tipoDescarte
    */
    // Relacionamento N:N com LocalDescarte
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "material_local",
        joinColumns = @JoinColumn(name = "materialId"),
        inverseJoinColumns = @JoinColumn(name = "localDescarteId")
    )
    @JsonIgnore
    private Set<EntityLocalDescarte> locaisDescarte = new HashSet<>();

    // Relacionamento 1:N com TipoDescarte
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipoDescarteId", nullable = false)
    @JsonIgnore
    private EntityTipoDescarte tipoDescarte;

}
