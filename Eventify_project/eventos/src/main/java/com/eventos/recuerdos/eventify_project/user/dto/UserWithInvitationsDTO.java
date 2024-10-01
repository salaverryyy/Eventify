package com.eventos.recuerdos.eventify_project.user.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;

import java.util.List;

public class UserWithInvitationsDTO {
    private Long id;
    private String username;
    private List<InvitationDTO> receivedInvitations;
    private List<InvitationDTO> sentInvitations;
}
//Mostrar al usuario junto con todas las invitaciones que ha recibido o enviado.