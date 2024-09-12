package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.evento.domain.Evento;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String contraseña;


    @OneToMany(mappedBy = "organizador")
    private List<Evento> eventosCreados;

    @ManyToMany
    @JoinTable(
            name = "usuario_evento",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "evento_id"))
    private List<Evento> eventosAsistidos;

}
