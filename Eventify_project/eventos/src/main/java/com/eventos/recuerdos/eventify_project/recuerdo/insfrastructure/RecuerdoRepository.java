package com.eventos.recuerdos.eventify_project.recuerdo.insfrastructure;

import com.eventos.recuerdos.eventify_project.recuerdo.domain.Recuerdo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuerdoRepository extends JpaRepository<Recuerdo, Long> {
    // Métodos adicionales si lo necesitas
}
