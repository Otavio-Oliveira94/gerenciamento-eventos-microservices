package com.eventosexpress.notificacoes.dto.response;

import com.eventosexpress.notificacoes.model.enums.CanalNotificacao;
import com.eventosexpress.notificacoes.model.enums.StatusNotificacao;
import com.eventosexpress.notificacoes.model.enums.TipoNotificacao;

import java.time.LocalDateTime;
import java.util.Map;

public record NotificacaoResponseDTO(
        String id,
        Long eventoId,
        TipoNotificacao tipo,
        CanalNotificacao canal,
        String destinatario,
        String assunto,
        String mensagem,
        StatusNotificacao status,
        LocalDateTime criadoEm,
        LocalDateTime enviadoEm,
        Map<String, Object> metadados
) {
}
