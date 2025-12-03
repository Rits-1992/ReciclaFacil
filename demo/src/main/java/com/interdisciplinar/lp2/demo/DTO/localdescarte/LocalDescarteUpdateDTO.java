package com.interdisciplinar.lp2.demo.DTO.localdescarte;
import java.time.LocalTime;
import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record LocalDescarteUpdateDTO(

    //@NotNull(message = "O ID é obrigatório")
    //Long id,

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    LocalTime horarioAbertura,
    LocalTime horarioFechamento,

    String contato,

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    String email,

    String descricao,

    Boolean situacao,
    EnderecoDTO endereco,
    Set<TipoDescarteDTO> tiposDescarte

) {

    public record EnderecoDTO(
        String rua,
        Integer numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
    ) {}

    public record TipoDescarteDTO(
        Long id,
        String nome
    ) {}
    
}