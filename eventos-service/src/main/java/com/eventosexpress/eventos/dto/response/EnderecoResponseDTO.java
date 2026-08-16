package com.eventosexpress.eventos.dto.response;

public record EnderecoResponseDTO(
        String logradouro,
        String numero,
        String complemento,
        String cep,
        String cidade,
        String estado
) {
}
