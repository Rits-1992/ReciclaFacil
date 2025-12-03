package com.interdisciplinar.lp2.demo.DTO.endereco;

public record EnderecoDTO(
    Long id,
    String rua,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep
) {}