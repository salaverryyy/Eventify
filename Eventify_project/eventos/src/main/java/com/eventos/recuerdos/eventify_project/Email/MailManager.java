package com.eventos.recuerdos.eventify_project.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailManager {
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public MailManager(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendMessage(String email, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String content = BienvenidaHtml.TEMPLATE_BIENVENIDA;

        try {
            mimeMessage.setSubject("Bienvenida a Eventify");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setText(message, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}