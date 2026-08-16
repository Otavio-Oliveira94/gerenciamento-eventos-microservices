package com.eventosexpress.eventos.dto.request;

import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
        @Size(max = 150, message = "O logradouro deve possuir no máximo 150 caracteres")
        String logradouro,

        @Size(max = 20, message = "O número deve possuir no máximo 20 caracteres")
        String numero,

        @Size(max = 100, message = "O complemento deve possuir no máximo 100 caracteres")
        String complemento,

        @Size(max = 20, message = "O CEP deve possuir no máximo 20 caracteres")
        String cep,

        @Size(max = 100, message = "A cidade deve possuir no máximo 100 caracteres")
        String cidade,

        @Size(max = 100, message = "O estado deve possuir no máximo 100 caracteres")
        String estado
) {
}
