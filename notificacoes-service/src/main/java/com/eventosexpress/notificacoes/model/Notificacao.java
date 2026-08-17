package com.eventosexpress.notificacoes.model;

import com.eventosexpress.notificacoes.model.enums.CanalNotificacao;
import com.eventosexpress.notificacoes.model.enums.StatusNotificacao;
import com.eventosexpress.notificacoes.model.enums.TipoNotificacao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "notificacoes")
public class Notificacao {
    @Id
    private String id;

    @Indexed
    private Long eventoId;

    private TipoNotificacao tipo;

    private CanalNotificacao canal;

    private String destinatario;

    private String assunto;

    private String mensagem;

    private StatusNotificacao status;

    @Indexed
    private LocalDateTime criadoEm;

    private LocalDateTime enviadoEm;

    private Map<String, Object> metadados = new LinkedHashMap<>();
}
