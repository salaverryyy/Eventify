package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvitationRequestDto {
    private String accessCode;
    private List<String> usernames;
}
