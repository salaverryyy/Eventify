package com.eventos.recuerdos.eventify_project.publication.domain;


import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //id unico
    private User user; //usuario que hace la publicacion
    private TipoArchivo tipoArchivo;// tipo de archivo(FOTO o VIDEO)
    private String archivo; //URL del archivo
    private String descripcion; //descripcion de la publicacion
    private LocalDate fechaPublicacion; //fecha de publicacion
    private int likes; //cantidad de Likes
    private List<Comment> comentarios; // lista de comentarios de la publicacion

}
