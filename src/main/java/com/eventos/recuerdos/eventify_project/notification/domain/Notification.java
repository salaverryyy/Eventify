package com.eventos.recuerdos.eventify_project.notification.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
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
    private NotificationType type; // Tipo de la notificación (INVITATION, REMINDER, NEW_PUBLICATION).

    private String message; // Mensaje del contenido de la notificación.

    private LocalDateTime sentDate; // Fecha y hora en la que se envió la notificación.

    @Enumerated(EnumType.STRING)
    private Status status; // Estado de la notificación (READ o UNREAD).

    // Relación Many-to-One con el Usuario receptor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccountReceiver;

    @Enumerated(EnumType.STRING)
    private RelatedWith relatedWith; // Relación con EVENT, MEMORY o PUBLICATION.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    private Event relatedEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", nullable = true)
    private Memory relatedMemory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = true)
    private Publication relatedPublication;
}
