package com.eventos.recuerdos.eventify_project.event.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventBasicDto {
    private Long id;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private Long organizerId;
}
