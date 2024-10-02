package com.eventos.recuerdos.eventify_project.notification.domain;

import com.eventos.recuerdos.eventify_project.notification.infrastructure.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
}
