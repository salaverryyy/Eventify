package com.eventos.recuerdos.eventify_project.evento.insfrastructure;

import com.eventos.recuerdos.eventify_project.evento.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Métodos adicionales si lo necesitas
}
