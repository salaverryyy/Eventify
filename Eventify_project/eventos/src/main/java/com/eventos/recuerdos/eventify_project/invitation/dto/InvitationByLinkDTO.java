package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationByLinkDTO {
    private Long userId;
    private String guestEmail;
    private String invitationLink;
    private Long eventId;// el token o enlace de la invitación
    private Long invitedUserId;
}
