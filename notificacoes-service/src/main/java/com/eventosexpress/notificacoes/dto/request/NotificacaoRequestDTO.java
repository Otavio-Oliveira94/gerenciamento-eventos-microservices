package com.eventosexpress.notificacoes.dto.request;

import com.eventosexpress.notificacoes.model.enums.CanalNotificacao;
import com.eventosexpress.notificacoes.model.enums.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record NotificacaoRequestDTO(
        @NotNull(message = "O ID do evento é obrigatório")
        @Positive(message = "O ID do evento deve ser positivo")
        Long eventoId,

        @NotNull(message = "O tipo da notificação é obrigatório")
        TipoNotificacao tipo,

        @NotNull(message = "O canal da notificação é obrigatório")
        CanalNotificacao canal,

        @NotBlank(message = "O destinatário é obrigatório")
        @Size(
                max = 150,
                message = "O destinatário deve possuir no máximo 150 caracteres"
        )
        String destinatario,

        @NotBlank(message = "O assunto é obrigatório")
        @Size(
                max = 200,
                message = "O assunto deve possuir no máximo 200 caracteres"
        )
        String assunto,

        @NotBlank(message = "A mensagem é obrigatória")
        @Size(
                max = 2000,
                message = "A mensagem deve possuir no máximo 2000 caracteres"
        )
        String mensagem,

        Map<String, Object> metadados
) {
}
