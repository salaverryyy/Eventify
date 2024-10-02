package com.eventos.recuerdos.eventify_project.notification.dto;


import lombok.Data;

@Data
public class NotificationReadDTO {
    private Long id;
    private String status;  // LEIDO, NO_LEIDO
}
//Manejar el estado de las notificaciones, serà útil para marcar como leídas.