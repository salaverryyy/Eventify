package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationByQrDTO {
    private Long userId;
    private String guestEmail;
    private String qrCode;
}
