package com.eventos.recuerdos.eventify_project.invitation.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import lombok.Data;

@Data
public class InvitationDto {
    private Long id;  // Identificador único de la invitación
    private String qrCode;  // Código QR en formato base64 para el enlace del álbum
    private String guestEmail;  // Correo del invitado
    private String albumLink;  // Enlace al álbum
    private String confirmationLink;  // Enlace para confirmar la invitación
}
