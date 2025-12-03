package com.interdisciplinar.lp2.demo.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "usuario")
public class EntityUsuario {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long id;

    @NotBlank(message = "O nome do usuário não pode ficar em branco")
    @Column(name = "nomeUsuario", length = 100, nullable = false)
    private String nome;

    @NotBlank(message = "O email do usuário não pode ficar em branco")
    @Email(message = "O email informado não é válido")
    @Column(name = "emailUsuario", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha do usuário não pode ficar em branco")
    @Column(name = "senhaUsuario", length = 255, nullable = false)
    private String senha;

    // 1 = ADMIN, 0 = USER
    @Enumerated(EnumType.STRING)
    @Column(name = "tipoUsuario", length = 50, nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "situacaoUsuario", nullable = false)
    private boolean situacao; // true = ativo, false = inativo

    @Column(name="dataCadastroUsuario", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

}