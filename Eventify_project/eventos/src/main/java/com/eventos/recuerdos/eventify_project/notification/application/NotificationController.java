package com.eventos.recuerdos.eventify_project.notification.application;


import com.eventos.recuerdos.eventify_project.notification.domain.NotificationService;
import com.eventos.recuerdos.eventify_project.notification.domain.NotificationType;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Obtener los detalles de una notificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Obtener solo las notificaciones no leídas de un usuario
    @GetMapping("/usuario/{userId}/noleidas")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByUser(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Eliminar una notificación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // Marcar una notificación como leída
    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }

    // Marcar todas las notificaciones como leídas para un usuario
    @PutMapping("/usuario/{userId}/leer")
    public ResponseEntity<Void> markAllNotificationsAsRead(@PathVariable Long userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }

    // Filtrar notificaciones por tipo
    @GetMapping("/tipo/{type}")
    public ResponseEntity<List<NotificationDTO>> filterNotificationsByType(@PathVariable NotificationType type) {
        List<NotificationDTO> notifications = notificationService.filterNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    // Desmarcar una notificación como no leída
    @PutMapping("/{id}/desmarcar")
    public ResponseEntity<Void> unmarkNotificationAsRead(@PathVariable Long id) {
        notificationService.unmarkNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }

    //Obtener todas las notificaciones existentes
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

}
