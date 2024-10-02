package com.eventos.recuerdos.eventify_project.user.domain;


import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;// identificador unico
    private String username; //nombre de usuario
    private String email;//correo electronico para autenticacion y envio de correo
    private String password; //contraseña para iniciar sesion
    private LocalDate userCreationDate;//fecha en que se creò el perfil

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Memory> memories = new ArrayList<>(); // Lista de recuerdos creados por el usuario

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invitation> invitations = new ArrayList<>(); // Lista de invitaciones recibidas o enviadas

    @OneToMany(mappedBy = "userReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();


}
