package com.eventos.recuerdos.eventify_project.event.dto;

import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {
    private Long id;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private String location;
    private MemoryDTO memory;
}
