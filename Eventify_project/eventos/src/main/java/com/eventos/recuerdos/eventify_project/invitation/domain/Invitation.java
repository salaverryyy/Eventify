package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "invitations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"event_id", "invited_user_id"})
})
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String invitationLink;

    @Column(columnDefinition = "TEXT")
    private String qrCode;
    // Enlace de invitación
    private String guestEmail;  // Correo del invitado (para invitados no registrados)

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;  // Estado de la invitación


    // Relación con el usuario que envía la invitación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount usuarioInvitador;

    // Relación con el usuario invitado (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_user_id", nullable = true)
    private UserAccount usuarioInvitado;

    // Relación con un evento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    // Relación con un recuerdo (Memory)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Memory memory;
}
