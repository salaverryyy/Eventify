package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

@Data
public class InvitationByLinkDTO {
    private Long userId;
    private Long eventId;
    private String invitationLink;
    private String guestEmail;
}