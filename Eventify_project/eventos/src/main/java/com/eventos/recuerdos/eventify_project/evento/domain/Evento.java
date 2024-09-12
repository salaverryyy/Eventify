package com.eventos.recuerdos.eventify_project.evento.domain;

import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.recuerdo.domain.Recuerdo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String fecha;
    private String ubicacion; // Coordenadas del evento (latitud, longitud)

    @Column(unique = true)
    private String qr; // Código QR único del evento

    private Boolean privacidad; // Público o privado

    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private User organizador;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Recuerdo> recuerdos;

}
