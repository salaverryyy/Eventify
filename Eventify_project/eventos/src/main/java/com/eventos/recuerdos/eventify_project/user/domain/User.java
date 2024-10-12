package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // identificador único
    private String username; // nombre de usuario
    private String email; // correo electrónico para autenticación y envío de correo
    private String password; // contraseña para iniciar sesión
    private LocalDate userCreationDate; // fecha en que se creó el perfil

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Memory> memories = new ArrayList<>(); // Lista de recuerdos creados por el usuario

    @OneToMany(mappedBy = "usuarioInvitador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invitation> invitations = new ArrayList<>(); // Lista de invitaciones recibidas o enviadas

    @OneToMany(mappedBy = "userReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    // Implementaciones de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí puedes agregar la lógica para obtener los roles o autoridades del usuario
        // Ejemplo: retornar una autoridad básica
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Cambia esto según tus necesidades
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementar lógica según sea necesario
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementar lógica según sea necesario
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; }

    @Override
    public boolean isEnabled() {
        return true;
    }}

