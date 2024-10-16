package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationByQrDTO {
    private Long userId;
    private Long eventId;
    private String qrCode;
    private String guestEmail;
    private Long invitedUserId;
}