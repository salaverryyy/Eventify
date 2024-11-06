package com.eventos.recuerdos.eventify_project.invitation.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvitationByQrDto {
    private Long userId;  // ID del usuario que envía la invitación
    private List<String> usernames;  // Lista de nombres de usuario de los invitados
}
