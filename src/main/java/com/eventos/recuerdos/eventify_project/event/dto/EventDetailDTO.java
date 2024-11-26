package com.eventos.recuerdos.eventify_project.event.dto;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class EventDetailDTO {
    private Long id;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private List<InvitationDto> invitations;
    private List<MemoryDTO> memories;
}
//Transferir información detallada de un evento,
// incluyendo invitaciones y recuerdos asociados.