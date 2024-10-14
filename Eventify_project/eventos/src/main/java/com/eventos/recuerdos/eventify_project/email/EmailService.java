package com.eventos.recuerdos.eventify_project.email;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import java.io.File;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            Context context = new Context();
            context.setVariable("message", text);

            String contentHTML = templateEngine.process("EmailTemplates", context);

            // Usa la ruta correcta y elimina el '0' extra
            File logoFile = new File("C:\\Users\\USUARIO\\Desktop\\Eventify\\Eventify_project\\eventos\\src\\main\\java\\com\\eventos\\recuerdos\\eventify_project\\email\\logo.png");
            if (!logoFile.exists()) {
                logger.warn("El archivo logo.png no existe en la ruta especificada: {}", logoFile.getAbsolutePath());
            } else {
                logger.info("El archivo logo.png fue encontrado exitosamente en: {}", logoFile.getAbsolutePath());
                FileSystemResource res = new FileSystemResource(logoFile);
                helper.addInline("logo2", res);
            }

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contentHTML, true);
            mailSender.send(mimeMessage);
            logger.info("Correo enviado exitosamente a {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo a {}: {}", to, e.getMessage(), e);
        }
    }
}
