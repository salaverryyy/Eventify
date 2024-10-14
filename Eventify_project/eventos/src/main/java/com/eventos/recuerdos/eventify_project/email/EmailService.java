package com.eventos.recuerdos.eventify_project.email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

            // Adjunta la imagen como recurso interno
            File imageFile = new File("C:/DAVID/CS/Ciclo4/DBP/Proyecto/LOGO/Eventify_LOGO.jpg");
            helper.addInline("logoImage", imageFile);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
