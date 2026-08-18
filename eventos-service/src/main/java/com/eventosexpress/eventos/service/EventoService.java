package com.eventosexpress.eventos.service;

import com.eventosexpress.eventos.dto.request.EnderecoRequestDTO;
import com.eventosexpress.eventos.dto.request.EventoRequestDTO;
import com.eventosexpress.eventos.dto.response.EventoResponseDTO;
import com.eventosexpress.eventos.exception.EventoNaoEncontradoException;
import com.eventosexpress.eventos.exception.RegraDeNegocioException;
import com.eventosexpress.eventos.integration.notificacoes.service.NotificacaoIntegracaoService;
import com.eventosexpress.eventos.mapper.EventoMapper;
import com.eventosexpress.eventos.model.Evento;
import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import com.eventosexpress.eventos.model.enums.StatusEvento;
import com.eventosexpress.eventos.repository.EventoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {
    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final NotificacaoIntegracaoService notificacaoIntegracaoService;

    @Transactional
    public EventoResponseDTO criar(EventoRequestDTO request) {
        validarDadosDoEvento(request);

        Evento evento = eventoMapper.toEntity(request);
        Evento eventoSalvo = eventoRepository.save(evento);

        return eventoMapper.toResponse(eventoSalvo);
    }

    private Evento buscarEntidadePorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(
                        () -> new EventoNaoEncontradoException(id)
                );
    }

    private void validarDadosDoEvento(EventoRequestDTO request) {
        validarDatas(
                request.dataHoraInicio(),
                request.dataHoraFim()
        );

        validarEndereco(
                request.modalidade(),
                request.endereco()
        );
    }

    private void validarDatas(
            LocalDateTime dataHoraInicio,
            LocalDateTime dataHoraFim
    ) {
        if (dataHoraInicio == null || dataHoraFim == null) {
            throw new RegraDeNegocioException(
                    "As datas de início e término são obrigatórias"
            );
        }

        if (!dataHoraFim.isAfter(dataHoraInicio)) {
            throw new RegraDeNegocioException(
                    "A data de término deve ser posterior à data de início"
            );
        }

        if (!dataHoraInicio.isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException(
                    "A data de início deve estar no futuro"
            );
        }
    }

    private void validarEndereco(
            ModalidadeEvento modalidade,
            EnderecoRequestDTO endereco
    ) {
        if (modalidade == null) {
            throw new RegraDeNegocioException(
                    "A modalidade do evento é obrigatória"
            );
        }

        if (modalidade == ModalidadeEvento.ONLINE) {
            return;
        }

        if (endereco == null
                || campoVazio(endereco.logradouro())
                || campoVazio(endereco.numero())
                || campoVazio(endereco.cep())
                || campoVazio(endereco.cidade())
                || campoVazio(endereco.estado())) {

            throw new RegraDeNegocioException(
                    "O endereço completo é obrigatório para eventos presenciais ou híbridos"
            );
        }
    }

    private void validarEventoEmRascunho(
            Evento evento,
            String operacao
    ) {
        if (evento.getStatus() != StatusEvento.RASCUNHO) {
            throw new RegraDeNegocioException(
                    "Somente eventos em RASCUNHO podem ser "
                            + operacao
            );
        }
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.isBlank();
    }

    public List<EventoResponseDTO> listarTodos() {
        Sort ordenacao = Sort.by(
                Sort.Direction.ASC,
                "dataHoraInicio"
        );

        return eventoRepository.findAll(ordenacao)
                .stream()
                .map(eventoMapper::toResponse)
                .toList();
    }

    public EventoResponseDTO buscarPorId(Long id) {
        Evento evento = buscarEntidadePorId(id);

        return eventoMapper.toResponse(evento);
    }

    @Transactional
    public EventoResponseDTO atualizar(
            Long id,
            EventoRequestDTO request
    ) {
        Evento evento = buscarEntidadePorId(id);

        validarEventoEmRascunho(
                evento,
                "atualizados"
        );

        validarDadosDoEvento(request);
        eventoMapper.updateEntity(request, evento);

        Evento eventoAtualizado =
                eventoRepository.save(evento);

        return eventoMapper.toResponse(eventoAtualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Evento evento = buscarEntidadePorId(id);

        validarEventoEmRascunho(
                evento,
                "excluídos"
        );

        eventoRepository.delete(evento);
    }

    @Transactional
    public EventoResponseDTO publicar(Long id) {
        Evento evento = buscarEntidadePorId(id);

        validarEventoEmRascunho(
                evento,
                "publicados"
        );

        validarDatas(
                evento.getDataHoraInicio(),
                evento.getDataHoraFim()
        );

        evento.setStatus(StatusEvento.PUBLICADO);

        Evento eventoPublicado = eventoRepository.save(evento);

        notificacaoIntegracaoService.notificarPublicacao(eventoPublicado);

        return eventoMapper.toResponse(eventoPublicado);
    }

    @Transactional
    public EventoResponseDTO cancelar(Long id) {
        Evento evento = buscarEntidadePorId(id);

        if (evento.getStatus() != StatusEvento.PUBLICADO) {
            throw new RegraDeNegocioException(
                    "Somente eventos PUBLICADOS podem ser cancelados"
            );
        }

        evento.setStatus(StatusEvento.CANCELADO);

        Evento eventoCancelado = eventoRepository.save(evento);

        notificacaoIntegracaoService.notificarCancelamento(eventoCancelado);

        return eventoMapper.toResponse(eventoCancelado);
    }

}
