package com.eventosexpress.notificacoes.mapper;

import com.eventosexpress.notificacoes.dto.request.NotificacaoRequestDTO;
import com.eventosexpress.notificacoes.dto.response.NotificacaoResponseDTO;
import com.eventosexpress.notificacoes.model.Notificacao;
import com.eventosexpress.notificacoes.model.enums.StatusNotificacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class NotificacaoMapper {
    public Notificacao toEntity(NotificacaoRequestDTO request) {
        Notificacao notificacao = new Notificacao();

        notificacao.setEventoId(request.eventoId());
        notificacao.setTipo(request.tipo());
        notificacao.setCanal(request.canal());
        notificacao.setDestinatario(request.destinatario());
        notificacao.setAssunto(request.assunto());
        notificacao.setMensagem(request.mensagem());
        notificacao.setStatus(StatusNotificacao.PENDENTE);
        notificacao.setCriadoEm(LocalDateTime.now());

        notificacao.setMetadados(
                request.metadados() == null
                        ? new LinkedHashMap<>()
                        : new LinkedHashMap<>(request.metadados())
        );

        return notificacao;
    }

    public NotificacaoResponseDTO toResponse(Notificacao notificacao) {
        Map<String, Object> metadados =
                notificacao.getMetadados() == null
                        ? Map.of()
                        : new LinkedHashMap<>(
                        notificacao.getMetadados()
                );

        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getEventoId(),
                notificacao.getTipo(),
                notificacao.getCanal(),
                notificacao.getDestinatario(),
                notificacao.getAssunto(),
                notificacao.getMensagem(),
                notificacao.getStatus(),
                notificacao.getCriadoEm(),
                notificacao.getEnviadoEm(),
                metadados
        );
    }
}
