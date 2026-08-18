package com.eventosexpress.eventos.dto.request;

import java.util.Map;

public record NotificacaoRequestDTO(
        Long eventoId,
        String tipo,
        String canal,
        String destinatario,
        String assunto,
        String mensagem,
        Map<String, Object> metadados
) {
}
