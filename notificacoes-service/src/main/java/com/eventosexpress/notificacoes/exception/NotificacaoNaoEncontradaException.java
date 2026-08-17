package com.eventosexpress.notificacoes.exception;

public class NotificacaoNaoEncontradaException extends RuntimeException {
    public NotificacaoNaoEncontradaException(String id) {
        super("Notificação não encontrada com o id: " + id);
    }
}
