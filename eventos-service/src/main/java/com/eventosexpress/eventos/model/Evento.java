package com.eventosexpress.eventos.model;

import com.eventosexpress.eventos.model.enums.ModalidadeEvento;
import com.eventosexpress.eventos.model.enums.StatusEvento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 200)
    private String subtitulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(name = "tipo_evento", nullable = false, length = 80)
    private String tipoEvento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModalidadeEvento modalidade;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "email_organizador", nullable = false, length = 150)
    private String emailOrganizador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEvento status = StatusEvento.RASCUNHO;

    @Embedded
    private Endereco endereco;
}
