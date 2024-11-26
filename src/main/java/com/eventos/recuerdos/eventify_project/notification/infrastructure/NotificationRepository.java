package com.eventos.recuerdos.eventify_project.notification.infrastructure;

import com.eventos.recuerdos.eventify_project.notification.domain.Notification;
import com.eventos.recuerdos.eventify_project.notification.domain.NotificationType;
import com.eventos.recuerdos.eventify_project.notification.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Buscar notificaciones por receptor
    List<Notification> findByUserAccountReceiverId(Long userId);

    // Buscar notificaciones no leídas por receptor
    List<Notification> findByUserAccountReceiverIdAndStatus(Long userId, Status status);

    // Filtrar notificaciones por tipo
    List<Notification> findByType(NotificationType type);
}
