package com.eventos.recuerdos.eventify_project.notification.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDate sentDate;
    private String status;
}
//Transferir la información de notificaciones enviadas a los usuarios