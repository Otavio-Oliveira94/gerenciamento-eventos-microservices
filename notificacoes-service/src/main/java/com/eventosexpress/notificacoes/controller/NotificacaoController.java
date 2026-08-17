package com.eventosexpress.notificacoes.controller;

import com.eventosexpress.notificacoes.dto.request.NotificacaoRequestDTO;
import com.eventosexpress.notificacoes.dto.response.NotificacaoResponseDTO;
import com.eventosexpress.notificacoes.service.NotificacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO> criarEEnviar(@Valid @RequestBody NotificacaoRequestDTO request) {
        NotificacaoResponseDTO response = notificacaoService.criarEEnviar(request);

        URI localizacao = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(localizacao)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(notificacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(notificacaoService.buscarPorId(id));
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> buscarPorEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(notificacaoService.buscarPorEvento(eventoId));
    }
}
