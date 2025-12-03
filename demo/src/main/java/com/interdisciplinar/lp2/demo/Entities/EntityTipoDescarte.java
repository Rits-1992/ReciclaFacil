package com.interdisciplinar.lp2.demo.Entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "tipoDescarte")
public class EntityTipoDescarte {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipoDescarte")
    private Long id;

    @NotBlank(message = "O nome da categoria não pode ser vazio")
    @Column(name = "nomeTipoDescarte", length = 100, nullable = false, unique = true)
    private String nome;


    /*   Relacionamentos
     *      tipoDescarte N:1 material
     *      tipoDescarte N:N localDescarte --> local_tipo
     */
    // Relacionamento N:1 com Material
    @OneToMany(mappedBy = "tipoDescarte", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EntityMaterial> material;


    // Relacionamento N:N com LocalDescarte
    @ManyToMany(mappedBy = "tiposDescarte", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EntityLocalDescarte> locaisDescarte;
}