package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationByQrDTO {
    private Long userId;   // Usuario que envía la invitación
    private String qrCode;
    private String guestEmail;
    private Long eventId;
    private Long invitedUserId;  // Opcional: ID del usuario invitado si ya existe
}
