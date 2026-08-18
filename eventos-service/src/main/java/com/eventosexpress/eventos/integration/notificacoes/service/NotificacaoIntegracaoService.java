package com.eventosexpress.eventos.integration.notificacoes.service;

import com.eventosexpress.eventos.dto.request.NotificacaoRequestDTO;
import com.eventosexpress.eventos.integration.notificacoes.client.NotificacaoClient;
import com.eventosexpress.eventos.model.Evento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoIntegracaoService {

    private static final String CIRCUIT_BREAKER = "notificacoesService";

    private final NotificacaoClient notificacaoClient;

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    private boolean enviarComResiliencia(NotificacaoRequestDTO request) {
        return circuitBreakerFactory
                .create(CIRCUIT_BREAKER)
                .run(
                        () -> {
                            notificacaoClient.criarEEnviar(request);

                            log.info(
                                    "Notificação {} enviada para o evento {}",
                                    request.tipo(),
                                    request.eventoId()
                            );

                            return true;
                        },
                        throwable -> {
                            log.warn(
                                    "Falha ao enviar notificação do evento {}. "
                                            + "A operação do evento foi mantida. Motivo: {}",
                                    request.eventoId(),
                                    throwable.getMessage()
                            );

                            return false;
                        }
                );
    }

    private NotificacaoRequestDTO criarRequest(
            Evento evento,
            String tipo,
            String assunto,
            String mensagem
    ) {
        Map<String, Object> metadados = new LinkedHashMap<>();

        metadados.put(
                "tituloEvento",
                evento.getTitulo()
        );

        metadados.put(
                "statusEvento",
                evento.getStatus().name()
        );

        metadados.put(
                "modalidade",
                evento.getModalidade().name()
        );

        metadados.put(
                "dataHoraInicio",
                evento.getDataHoraInicio().toString()
        );

        metadados.put(
                "origem",
                "eventos-service"
        );

        return new NotificacaoRequestDTO(
                evento.getId(),
                tipo,
                "EMAIL",
                evento.getEmailOrganizador(),
                assunto,
                mensagem,
                metadados
        );
    }

    public boolean notificarPublicacao(Evento evento) {
        NotificacaoRequestDTO request = criarRequest(
                evento,
                "EVENTO_PUBLICADO",
                "Evento publicado: " + evento.getTitulo(),
                "O evento " + evento.getTitulo()
                        + " foi publicado com sucesso."
        );

        return enviarComResiliencia(request);
    }

    public boolean notificarCancelamento(Evento evento) {
        NotificacaoRequestDTO request = criarRequest(
                evento,
                "EVENTO_CANCELADO",
                "Evento cancelado: " + evento.getTitulo(),
                "O evento " + evento.getTitulo()
                        + " foi cancelado."
        );

        return enviarComResiliencia(request);
    }
}
