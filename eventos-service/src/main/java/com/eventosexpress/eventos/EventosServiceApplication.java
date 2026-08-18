package com.eventosexpress.eventos;

import com.eventosexpress.eventos.integration.notificacoes.client.NotificacaoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = NotificacaoClient.class)
public class EventosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventosServiceApplication.class, args);
    }

}
