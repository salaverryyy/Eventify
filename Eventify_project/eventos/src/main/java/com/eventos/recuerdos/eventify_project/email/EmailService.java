package com.eventos.recuerdos.eventify_project.email;
import com.eventos.recuerdos.eventify_project.controllers.StorageService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true para adjuntar archivos

            // Configuración del contexto para Thymeleaf
            Context context = new Context();
            context.setVariable("message", text);

            // Procesa el contenido del HTML usando Thymeleaf
            String contentHTML = templateEngine.process("WelcomeTemplate", context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contentHTML, true); // true para indicar que es HTML

            try {
                // Generate the presigned URL
                String presignedUrl = storageService.generatePresignedUrl("eventiyLogo.jpg");

                // Fetch the resource from the URL
                UrlResource resource = new UrlResource(presignedUrl);

                // Attach the resource to the email as an inline image
                helper.addInline("logoImage", resource, "image/jpeg");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error attaching logo to email", e);
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendInvitationMessage(String to, String subject, String albumLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Configuración del contexto para Thymeleaf
            Context context = new Context();
            context.setVariable("albumLink", albumLink);

            // Procesa el contenido del HTML usando Thymeleaf
            String contentHTML = templateEngine.process("invitation-email-template", context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contentHTML, true);

            try {
                // Generate the presigned URL
                String presignedUrl = storageService.generatePresignedUrl("eventiyLogo.jpg");

                // Fetch the resource from the URL
                UrlResource resource = new UrlResource(presignedUrl);

                // Attach the resource to the email as an inline image
                helper.addInline("logoImage", resource, "image/jpeg");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error attaching logo to email", e);
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
