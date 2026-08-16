package com.eventosexpress.eventos.mapper;

import com.eventosexpress.eventos.dto.request.EnderecoRequestDTO;
import com.eventosexpress.eventos.dto.request.EventoRequestDTO;
import com.eventosexpress.eventos.dto.response.EventoResponseDTO;
import com.eventosexpress.eventos.model.Evento;
import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import com.eventosexpress.eventos.model.enums.StatusEvento;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class EventoMapperTest {
    private final EventoMapper eventoMapper = new EventoMapper();

    private EventoRequestDTO criarRequest(String titulo) {
        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
                "Av Paulista",
                "1578",
                "Masp",
                "01310-200",
                "São Paulo",
                "SP"
        );

        return new EventoRequestDTO(
                titulo,
                "Spring Boot e Spring Cloud",
                "Evento acadêmico sobre aplicações distribuídas.",
                "WORKSHOP",
                ModalidadeEvento.HIBRIDO,
                LocalDateTime.of(2027, 10, 10, 9, 0),
                LocalDateTime.of(2027, 10, 10, 17, 0),
                "organizador@email.com",
                endereco
        );
    }

    @Test
    void deveConverterRequestDTOParaEntidade() {
        EventoRequestDTO request = criarRequest("Workshop de Microservices");

        Evento evento = eventoMapper.toEntity(request);

        assertThat(evento.getId()).isNull();
        assertThat(evento.getTitulo())
                .isEqualTo("Workshop de Microservices");
        assertThat(evento.getModalidade())
                .isEqualTo(ModalidadeEvento.HIBRIDO);
        assertThat(evento.getStatus())
                .isEqualTo(StatusEvento.RASCUNHO);
        assertThat(evento.getEndereco()).isNotNull();
        assertThat(evento.getEndereco().getCidade())
                .isEqualTo("São Paulo");
    }

    @Test
    void deveConverterEntidadeParaResponseDTO() {
        Evento evento = eventoMapper.toEntity(
                criarRequest("Workshop de Microservices")
        );

        evento.setId(10L);
        evento.setStatus(StatusEvento.PUBLICADO);

        EventoResponseDTO response = eventoMapper.toResponse(evento);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.titulo())
                .isEqualTo("Workshop de Microservices");
        assertThat(response.status())
                .isEqualTo(StatusEvento.PUBLICADO);
        assertThat(response.endereco()).isNotNull();
        assertThat(response.endereco().cidade())
                .isEqualTo("São Paulo");
    }

    @Test
    void deveAtualizarEventoSemModificarIdEStatus() {
        Evento evento = eventoMapper.toEntity(
                criarRequest("Evento original")
        );

        evento.setId(20L);
        evento.setStatus(StatusEvento.PUBLICADO);

        EventoRequestDTO requestAtualizado =
                criarRequest("Evento atualizado");

        eventoMapper.updateEntity(requestAtualizado, evento);

        assertThat(evento.getId()).isEqualTo(20L);
        assertThat(evento.getTitulo())
                .isEqualTo("Evento atualizado");
        assertThat(evento.getStatus())
                .isEqualTo(StatusEvento.PUBLICADO);
    }
}
