package com.eventos.recuerdos.eventify_project.email;

import com.eventos.recuerdos.eventify_project.InvitationEmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class InvitationEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void handleInvitationEmailEvent(InvitationEmailEvent event) {
        String to = event.getEmail();
        String subject = "Invitación al Álbum Virtual de Eventify";
        String albumLink = event.getAlbumLink();
        emailService.sendInvitationMessage(to, subject, albumLink);
    }
}

