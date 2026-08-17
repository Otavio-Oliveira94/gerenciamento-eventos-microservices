package com.eventosexpress.eventos.controller;

import com.eventosexpress.eventos.dto.request.EventoRequestDTO;
import com.eventosexpress.eventos.dto.response.EventoResponseDTO;
import com.eventosexpress.eventos.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {
    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponseDTO> criar(
            @Valid @RequestBody EventoRequestDTO request
    ) {
        EventoResponseDTO response =
                eventoService.criar(request);

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
    public ResponseEntity<List<EventoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(
                eventoService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                eventoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequestDTO request
    ) {
        return ResponseEntity.ok(
                eventoService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {
        eventoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publicar")
    public ResponseEntity<EventoResponseDTO> publicar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                eventoService.publicar(id)
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EventoResponseDTO> cancelar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                eventoService.cancelar(id)
        );
    }
}
