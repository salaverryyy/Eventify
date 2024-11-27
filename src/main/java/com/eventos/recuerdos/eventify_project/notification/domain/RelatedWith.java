package com.eventos.recuerdos.eventify_project.notification.domain;

public enum RelatedWith {
    USER,                  // Relacionado con un usuario (por ejemplo, un nuevo seguidor).
    EVENT,                 // Relacionado con un evento (por ejemplo, un evento próximo o recordatorio).
    INVITATION,            // Relacionado con una invitación (nueva invitación recibida).
    MEMORY,                // Relacionado con un recuerdo o álbum virtual.
    PUBLICATION,           // Relacionado con una nueva publicación (foto o video).
    COMMENT,               // Relacionado con un comentario en una publicación.
    LIKE                   // Relacionado con un "me gusta" en una publicación.
}
