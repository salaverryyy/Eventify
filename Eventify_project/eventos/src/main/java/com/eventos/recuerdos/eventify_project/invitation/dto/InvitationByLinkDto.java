package com.eventos.recuerdos.eventify_project.invitation.dto;


import lombok.Data;

import java.util.List;

@Data
public class InvitationByLinkDto {
    private Long userId;
    private Long eventId;
    private List<String> guestEmails;  // Lista de correos de los invitados
}