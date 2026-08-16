package com.eventosexpress.eventos.repository;

import com.eventosexpress.eventos.model.Endereco;
import com.eventosexpress.eventos.model.Evento;
import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import com.eventosexpress.eventos.model.enums.StatusEvento;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventoRepositoryTest {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deveSalvarEBuscarEventoNoPostgreSQL() {

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Av Paulista");
        endereco.setNumero("1578");
        endereco.setComplemento("Masp");
        endereco.setCep("01310-200");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");

        Evento evento = new Evento();
        evento.setTitulo("Workshop de Microservices");
        evento.setSubtitulo("Spring Boot e Spring Cloud");
        evento.setDescricao(
                "Evento acadêmico sobre desenvolvimento de aplicações distribuídas."
        );
        evento.setTipoEvento("WORKSHOP");
        evento.setModalidade(ModalidadeEvento.HIBRIDO);
        evento.setDataHoraInicio(
                LocalDateTime.of(2026, 10, 10, 9, 0)
        );
        evento.setDataHoraFim(
                LocalDateTime.of(2026, 10, 10, 17, 0)
        );
        evento.setEmailOrganizador("organizador@email.com");
        evento.setStatus(StatusEvento.RASCUNHO);
        evento.setEndereco(endereco);

        Evento eventoSalvo = eventoRepository.saveAndFlush(evento);

        entityManager.clear();

        Optional<Evento> eventoEncontrado =
                eventoRepository.findById(eventoSalvo.getId());

        assertThat(eventoSalvo.getId()).isNotNull();
        assertThat(eventoEncontrado).isPresent();

        Evento eventoPersistido = eventoEncontrado.get();

        assertThat(eventoPersistido.getTitulo())
                .isEqualTo("Workshop de Microservices");

        assertThat(eventoPersistido.getModalidade())
                .isEqualTo(ModalidadeEvento.HIBRIDO);

        assertThat(eventoPersistido.getStatus())
                .isEqualTo(StatusEvento.RASCUNHO);

        assertThat(eventoPersistido.getEndereco().getCidade())
                .isEqualTo("São Paulo");
    }
}
