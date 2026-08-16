package com.eventosexpress.eventos.repository;

import com.eventosexpress.eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
