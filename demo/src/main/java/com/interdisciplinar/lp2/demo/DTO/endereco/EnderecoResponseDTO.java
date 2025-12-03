package com.interdisciplinar.lp2.demo.DTO.endereco;

import lombok.Data;

@Data
public class EnderecoResponseDTO {
    private Long id;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
}
