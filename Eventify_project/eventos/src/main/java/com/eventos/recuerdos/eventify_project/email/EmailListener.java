package com.eventos.recuerdos.eventify_project.email;

import com.eventos.recuerdos.eventify_project.HelloEmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void handleHelloEmailEvent(HelloEmailEvent event) {
        String to = event.getEmail();
        String subject = "Mensaje de Bienvenida a Eventify";
        String text = "Este es un correo siiii.";
        emailService.sendSimpleMessage(to, subject,text);
    }

}