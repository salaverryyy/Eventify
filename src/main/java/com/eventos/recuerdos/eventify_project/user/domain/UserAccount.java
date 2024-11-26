package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.notification.domain.Notification;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único

    private String firstName;  // Nombre del usuario
    private String lastName;   // Apellido del usuario

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email; // Correo electrónico único para autenticación

    @Column(nullable = false)
    private String password; // Contraseña para iniciar sesión

    private String profilePictureKey;

    private LocalDate userCreationDate = LocalDate.now(); // Fecha de creación del perfil

    @Enumerated(EnumType.STRING)
    private Role role; // Rol del usuario

    private Boolean expired = false;
    private Boolean locked = false;
    private Boolean credentialsExpired = false;
    private Boolean enable = true;

    // Nueva relación para amigos
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<UserAccount> friends = new HashSet<>(); // Lista de amigos

    // Relaciones con otras entidades

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Memory> memories = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInvitador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Invitation> invitationsSent = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioInvitado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Invitation> invitationsReceived = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Invitation> acceptedInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "userAccountReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Publication> publications = new ArrayList<>();

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getUsernameField() {
        return username; // Devuelve el nombre de usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    // Métodos para gestionar la lista de amigos
    public void addFriend(UserAccount friend) {
        friends.add(friend);
        friend.getFriends().add(this); // Añadir reciprocidad
    }

    public void removeFriend(UserAccount friend) {
        friends.remove(friend);
        friend.getFriends().remove(this); // Eliminar reciprocidad
    }
}

