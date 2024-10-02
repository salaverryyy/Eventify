package com.eventos.recuerdos.eventify_project.invitation.dto;


import lombok.Data;

@Data
public class InvitationDTO {
    private Long id;
    private String qrCode;
    private String invitationLink;
    private String guestEmail;
    private String status;
}

//gestionar la transferencia de invitaciones, incluyendo estado y enlaces