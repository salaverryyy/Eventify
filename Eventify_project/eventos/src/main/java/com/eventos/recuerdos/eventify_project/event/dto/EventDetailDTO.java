package com.eventos.recuerdos.eventify_project.event.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;

import java.time.LocalDate;
import java.util.List;

public class EventDetailDTO {
    private Long id;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private List<InvitationDTO> invitations;
    private List<MemoryDTO> memories;
}
//Transferir información detallada de un evento,
// incluyendo invitaciones y recuerdos asociados.