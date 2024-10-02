package com.eventos.recuerdos.eventify_project.notification.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // Tipo de la notificación (invitación, recordatorio, nueva publicación).

    private String message; // Mensaje del contenido de la notificación.

    private LocalDateTime sentDate; // Fecha y hora en la que se envió la notificación.

    @Enumerated(EnumType.STRING)
    private Status status; // Estado de la notificación (leída o no leída).

    // Relación Many-to-One con User, el receptor de la notificación.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userReceiver; // Usuario que recibe la notificación.

    @Enumerated(EnumType.STRING)
    private RelatedWith relatedWith; // Indica con qué entidad está relacionada la notificación.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    private Event relatedEvent; // Relación con Event (si la notificación está relacionada con un evento)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", nullable = true)
    private Memory relatedMemory; // Relación con Memory (si la notificación está relacionada con un recuerdo)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = true)
    private Publication relatedPublication; // Relación con Publication (si la notificación está relacionada con una publicación)
}
