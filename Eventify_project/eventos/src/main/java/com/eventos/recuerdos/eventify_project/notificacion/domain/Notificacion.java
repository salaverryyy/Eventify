package com.eventos.recuerdos.eventify_project.notificacion.domain;

import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.evento.domain.Evento;
import jakarta.persistence.*;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mensaje;
    private String fechaEnvio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

}
