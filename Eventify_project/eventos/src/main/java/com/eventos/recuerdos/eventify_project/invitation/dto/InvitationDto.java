package com.eventos.recuerdos.eventify_project.invitation.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import lombok.Data;

@Data
public class InvitationDto {
    private Long id;  // Identificador único de la invitación
    private String qrCode;  // Código QR en formato base64 para el enlace del álbum
    private String invitationLink;  // Enlace único de invitación al álbum
    private String guestEmail;  // Correo del invitado
    private Long userId;  // ID del usuario que envió la invitación (organizador)
    private Long invitedUserId;  // ID del usuario invitado (si es un usuario registrado)
    private Long eventId;  // ID del evento asociado a la invitación
}

