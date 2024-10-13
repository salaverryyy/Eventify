package com.eventos.recuerdos.eventify_project.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {
    private String email;
    private String subject;
    private String body;
}
