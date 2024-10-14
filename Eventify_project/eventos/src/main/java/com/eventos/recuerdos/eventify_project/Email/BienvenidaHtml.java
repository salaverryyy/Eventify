package com.eventos.recuerdos.eventify_project.Email;

public class BienvenidaHtml {
    public static final String TEMPLATE_BIENVENIDA =
            "<html lang='es'>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "<title>Bienvenido a Eventify</title>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                    ".container { background-color: #ffffff; max-width: 600px; margin: 20px auto; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }" +
                    ".header { text-align: center; padding: 20px; }" +
                    ".header img { max-width: 150px; }" +
                    ".content { text-align: center; padding: 20px; }" +
                    ".content h1 { color: #333333; }" +
                    ".content p { font-size: 16px; color: #555555; }" +
                    ".cta-button { margin-top: 20px; padding: 15px 30px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; border-radius: 5px; }" +
                    ".footer { margin-top: 30px; text-align: center; color: #888888; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<img src='https://ibb.co/8DsvzqS' alt='Logo de Eventify'>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<h1>¡Bienvenido, {{username}}!</h1>" +  // Usamos el username
                    "<p>Estamos muy emocionados de que te hayas unido a <strong>Eventify</strong>. ¡Gracias por registrarte!</p>" +
                    "<p>A partir de ahora, podrás disfrutar de todas las funciones que hemos diseñado especialmente para ti. Nos encantaría que empezaras explorando todo lo que ofrecemos.</p>" +
                    "<p>Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.</p>" +
                    "<a href='https://eventify.com/inicio' class='cta-button'>Comienza ahora</a>" +
                    "</div>" +
                    "<div class='footer'>" +
                    "<p>&copy; 2024 Eventify. Todos los derechos reservados.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
}
