// FALTA APENAS CORRIGIR ERROS DE RELACIONAMENTO
package com.interdisciplinar.lp2.demo.Entities;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "local_descarte")
public class EntityLocalDescarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLocalDescarte")
    private Long id;

    @Column(name = "nomeLocalDescarte", length = 150, nullable = false)
    @NotBlank(message = "O nome do local de descarte é obrigatório")
    private String nome;

    @Column(name = "horarioAbertura")
    private LocalTime horarioAbertura;

    @Column(name = "horarioFechamento")
    private LocalTime horarioFechamento;

    @Pattern(
        regexp = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$",
        message = "Formato de WhatsApp inválido. Use (XX) XXXXX-XXXX ou (XX) XXXX-XXXX"
    )
    @Column(name = "whatsappLocalDescarte", length = 16)
    private String contato;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Column(name = "contatoEmail", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "descricaoLocalDescarte", length = 200)
    private String descricao;

    @Column(name = "situacaoLocalDescarte", nullable = false)
    private boolean situacao = true; // padrão: ativo

    /* RELACIONAMENTOS */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "enderecoId", nullable = false)
    private EntityEndereco endereco;

    @JsonIgnore
    @ManyToMany(mappedBy = "locaisDescarte")
    private Set<EntityMaterial> materiais = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "local_tipo",
        joinColumns = @JoinColumn(name = "localDescarteId"),
        inverseJoinColumns = @JoinColumn(name = "tipoDescarteId")
    )
    private Set<EntityTipoDescarte> tiposDescarte = new HashSet<>();
}
