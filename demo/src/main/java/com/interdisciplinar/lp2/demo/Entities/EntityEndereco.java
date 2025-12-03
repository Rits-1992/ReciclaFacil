package com.interdisciplinar.lp2.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endereco")
public class EntityEndereco {

    // Atributos
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "idEndereco")
    private Long id;

    @Column(name = "logradouro", length = 200, nullable = false)
    private String rua;

    @Column(name = "numeroEndereco", length = 20, nullable = true)
    private Integer numero;

    @Column(name = "complemento", length = 100, nullable = true)
    private String complemento;

    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;

    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade = "São José do Rio Preto";

    @Column(name = "estado", length = 30, nullable = false)
    private String estado = "São Paulo";

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    @Column(name = "cep", length = 9, nullable = true)
    private String cep;


    /** Relacionamentos
     *      endereco 1:1 localDescarte
     */
    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    @JsonIgnore
    private EntityLocalDescarte localDescarte;


}