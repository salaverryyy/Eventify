package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único de la invitación

    private String qrCode; // Código QR para escanear
    private String invitationLink; // Link de invitación para aceptar/rechazar
    private String guestEmail; // Correo del invitado
    private String status; // Estado de la invitación (por ejemplo: aceptada, pendiente, rechazada)

    // Relaciones
    @ManyToOne
    private User inviterUser; // Usuario que envía la invitación

    @ManyToOne
    private Memory memory; // Recuerdo asociado a la invitación

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event; // Evento asociado, si aplica
}
