package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String qrCode;
    private String invitationLink;
    private String guestEmail;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuarioInvitador;  // Quien envía la invitación

    @ManyToOne
    @JoinColumn(name = "invited_user_id", nullable = true)  // Usuario invitado (opcional si no está registrado)
    private User usuarioInvitado;   // Usuario invitado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    // Agregar la relación con Memory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    private Memory memory;  // Relación con Memory
}
