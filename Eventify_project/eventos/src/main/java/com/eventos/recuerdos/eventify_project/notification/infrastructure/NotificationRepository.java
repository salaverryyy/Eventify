package com.eventos.recuerdos.eventify_project.notification.infrastructure;

import com.eventos.recuerdos.eventify_project.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
