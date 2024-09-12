package com.eventos.recuerdos.eventify_project.notificacion.infrastructure;

import com.eventos.recuerdos.eventify_project.notificacion.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Métodos personalizados si los necesitas
}
