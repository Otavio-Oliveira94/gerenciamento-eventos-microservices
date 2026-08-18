package com.eventosexpress.eventos.integration.notificacoes.client;

import com.eventosexpress.eventos.dto.request.NotificacaoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacoes-service", path = "/api/notificacoes")
public interface NotificacaoClient {
    @PostMapping
    void criarEEnviar(@RequestBody NotificacaoRequestDTO request);
}
