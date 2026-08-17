package com.eventosexpress.notificacoes.exception.handler;

import com.eventosexpress.notificacoes.dto.response.ErroResponseDTO;
import com.eventosexpress.notificacoes.exception.NotificacaoNaoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ErroResponseDTO criarErro(
            HttpStatus status,
            String mensagem,
            String caminho,
            Map<String, String> campos
    ) {
        return new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                caminho,
                campos
        );
    }

    @ExceptionHandler(NotificacaoNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> tratarNaoEncontrada(
            NotificacaoNaoEncontradaException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status)
                .body(criarErro(
                        status,
                        exception.getMessage(),
                        request.getRequestURI(),
                        Map.of()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarCamposInvalidos(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> campos = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> campos.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return ResponseEntity.status(status)
                .body(criarErro(
                        status,
                        "Existem campos inválidos na requisição",
                        request.getRequestURI(),
                        campos
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(criarErro(
                        status,
                        "JSON inválido. Verifique os valores e o formato dos campos",
                        request.getRequestURI(),
                        Map.of()
                ));
    }
}
