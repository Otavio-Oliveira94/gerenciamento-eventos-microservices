package com.eventosexpress.notificacoes.service;

import com.eventosexpress.notificacoes.dto.request.NotificacaoRequestDTO;
import com.eventosexpress.notificacoes.dto.response.NotificacaoResponseDTO;
import com.eventosexpress.notificacoes.exception.NotificacaoNaoEncontradaException;
import com.eventosexpress.notificacoes.mapper.NotificacaoMapper;
import com.eventosexpress.notificacoes.model.Notificacao;
import com.eventosexpress.notificacoes.model.enums.StatusNotificacao;
import com.eventosexpress.notificacoes.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoMapper notificacaoMapper;

    private void simularEnvio(Notificacao notificacao) {
        notificacao.setStatus(StatusNotificacao.ENVIADA);
        notificacao.setEnviadoEm(LocalDateTime.now());
    }

    public NotificacaoResponseDTO criarEEnviar(
            NotificacaoRequestDTO request
    ) {
        Notificacao notificacao =
                notificacaoMapper.toEntity(request);

        Notificacao notificacaoPendente =
                notificacaoRepository.save(notificacao);

        simularEnvio(notificacaoPendente);

        Notificacao notificacaoEnviada =
                notificacaoRepository.save(notificacaoPendente);

        return notificacaoMapper.toResponse(
                notificacaoEnviada
        );
    }

    public List<NotificacaoResponseDTO> listarTodas() {
        return notificacaoRepository
                .findAllByOrderByCriadoEmDesc()
                .stream()
                .map(notificacaoMapper::toResponse)
                .toList();
    }

    public NotificacaoResponseDTO buscarPorId(String id) {
        Notificacao notificacao =
                notificacaoRepository.findById(id)
                        .orElseThrow(
                                () -> new NotificacaoNaoEncontradaException(
                                        id
                                )
                        );

        return notificacaoMapper.toResponse(notificacao);
    }

    public List<NotificacaoResponseDTO> buscarPorEvento(
            Long eventoId
    ) {
        return notificacaoRepository
                .findByEventoIdOrderByCriadoEmDesc(eventoId)
                .stream()
                .map(notificacaoMapper::toResponse)
                .toList();
    }
}
