package com.interdisciplinar.lp2.demo.DTO.localdescarte;

import java.time.LocalTime;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LocalDescarteCreateDTO(

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    LocalTime horarioAbertura,
    LocalTime horarioFechamento,

    String contato,

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    String email,

    String descricao,

    EnderecoDTO endereco,

    @NotEmpty(message = "É necessário informar ao menos um tipo de descarte")
    Set<Long> tiposDescarteIds
) {

    public record EnderecoDTO(
    @NotBlank(message = "A rua é obrigatória")
    String rua,

    @NotNull(message = "O número é obrigatório")
    Integer numero,

    String complemento,

    @NotBlank(message = "O bairro é obrigatório")
    String bairro,

    @NotBlank(message = "A cidade é obrigatória")
    String cidade,

    @NotBlank(message = "O estado é obrigatório")
    String estado,

    @NotBlank(message = "O cep é obrigatório")
    String cep
    ) {}

}