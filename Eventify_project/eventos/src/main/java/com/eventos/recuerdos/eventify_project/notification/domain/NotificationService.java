package com.eventos.recuerdos.eventify_project.notification.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.notification.infrastructure.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ModelMapper modelMapper;

    public NotificationService(NotificationRepository notificationRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.modelMapper = modelMapper;
    }

    // Obtener notificación por ID
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
        return modelMapper.map(notification, NotificationDTO.class);
    }

    // Obtener todas las notificaciones de un usuario
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserAccountReceiverId(userId);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    // Obtener solo las notificaciones no leídas de un usuario
    public List<NotificationDTO> getUnreadNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserAccountReceiverIdAndStatus(userId, Status.UNREAD);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    // Eliminar notificación por ID
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
        notificationRepository.delete(notification);
    }

    // Marcar una notificación como leída
    public void markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
        notification.setStatus(Status.READ);
        notificationRepository.save(notification);
    }

    // Marcar todas las notificaciones como leídas para un usuario
    public void markAllNotificationsAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserAccountReceiverIdAndStatus(userId, Status.UNREAD);
        for (Notification notification : notifications) {
            notification.setStatus(Status.READ);
        }
        notificationRepository.saveAll(notifications);
    }

    // Filtrar notificaciones por tipo
    public List<NotificationDTO> filterNotificationsByType(NotificationType type) {
        List<Notification> notifications = notificationRepository.findByType(type);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    // Desmarcar una notificación como no leída
    public void unmarkNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + id));
        notification.setStatus(Status.UNREAD);
        notificationRepository.save(notification);
    }

    //Obtener todas las notificaciones
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }
}

