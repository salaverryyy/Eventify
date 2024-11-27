package com.eventos.recuerdos.eventify_project.event.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventBasicDto {
    private Long eventId;
    private String eventName;
    private LocalDate eventDate;
    private String location;
}
