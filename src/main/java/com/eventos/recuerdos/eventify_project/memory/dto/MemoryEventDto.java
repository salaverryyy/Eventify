package com.eventos.recuerdos.eventify_project.memory.dto;


import com.eventos.recuerdos.eventify_project.event.dto.EventBasicDto;
import lombok.Data;

@Data
public class MemoryEventDto {
    private Long memoryId;
    private String coverPhoto;
    private EventBasicDto event;

}
