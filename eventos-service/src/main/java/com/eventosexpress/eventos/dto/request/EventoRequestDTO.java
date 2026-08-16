package com.eventosexpress.eventos.dto.request;

import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventoRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(
                max = 150,
                message = "O título deve possuir no máximo 150 caracteres"
        )
        String titulo,

        @Size(
                max = 200,
                message = "O subtítulo deve possuir no máximo 200 caracteres"
        )
        String subtitulo,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(
                max = 2000,
                message = "A descrição deve possuir no máximo 2000 caracteres"
        )
        String descricao,

        @NotBlank(message = "O tipo do evento é obrigatório")
        @Size(
                max = 80,
                message = "O tipo do evento deve possuir no máximo 80 caracteres"
        )
        String tipoEvento,

        @NotNull(message = "A modalidade é obrigatória")
        ModalidadeEvento modalidade,

        @NotNull(message = "A data e hora de início são obrigatórias")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "A data e hora de término são obrigatórias")
        LocalDateTime dataHoraFim,

        @NotBlank(message = "O e-mail do organizador é obrigatório")
        @Email(message = "O e-mail do organizador deve ser válido")
        @Size(
                max = 150,
                message = "O e-mail deve possuir no máximo 150 caracteres"
        )
        String emailOrganizador,

        @Valid
        EnderecoRequestDTO endereco
) {
}
