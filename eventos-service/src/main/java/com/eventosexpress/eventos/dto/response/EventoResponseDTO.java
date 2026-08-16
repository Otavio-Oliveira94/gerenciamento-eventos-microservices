package com.eventosexpress.eventos.dto.response;

import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import com.eventosexpress.eventos.model.enums.StatusEvento;

import java.time.LocalDateTime;

public record EventoResponseDTO(
        Long id,
        String titulo,
        String subtitulo,
        String descricao,
        String tipoEvento,
        ModalidadeEvento modalidade,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String emailOrganizador,
        StatusEvento status,
        EnderecoResponseDTO endereco
) {
}
