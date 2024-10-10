package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationStatusDTO {
    private Long id;  // ID de la invitación
    private String status;  // Estado de la invitación (por ejemplo, Aceptada, Pendiente)
}
