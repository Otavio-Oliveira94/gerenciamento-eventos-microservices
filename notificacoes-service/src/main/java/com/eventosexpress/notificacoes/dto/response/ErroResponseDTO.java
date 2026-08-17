package com.eventosexpress.notificacoes.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
