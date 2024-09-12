package com.eventos.recuerdos.eventify_project.recuerdo.domain;

import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.evento.domain.Evento;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Recuerdo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tipo; // Foto o Video
    private String url; // URL del archivo en la nube

    private String fechaSubida;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

}
