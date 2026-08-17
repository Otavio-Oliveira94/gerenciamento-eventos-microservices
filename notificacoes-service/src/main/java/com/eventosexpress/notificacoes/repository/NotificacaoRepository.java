package com.eventosexpress.notificacoes.repository;

import com.eventosexpress.notificacoes.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {
    List<Notificacao> findAllByOrderByCriadoEmDesc();

    List<Notificacao> findByEventoIdOrderByCriadoEmDesc(Long eventoId);
}
