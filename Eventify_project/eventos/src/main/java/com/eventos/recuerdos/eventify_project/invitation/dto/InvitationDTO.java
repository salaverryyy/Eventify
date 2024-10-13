package com.eventos.recuerdos.eventify_project.invitation.dto;


import lombok.Data;
import lombok.Data;

@Data
public class InvitationDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private String status;
}



//gestionar la transferencia de invitaciones, incluyendo estado y enlaces