package com.eventos.recuerdos.eventify_project.user.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private List<MemoryDTO> memories;
    private List<InvitationDTO> invitations;
}
//Mostrar información más detallada del usuario, con sus recuerdos e invitaciones.