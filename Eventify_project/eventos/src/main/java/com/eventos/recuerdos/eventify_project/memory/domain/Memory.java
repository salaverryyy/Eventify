package com.eventos.recuerdos.eventify_project.memory.domain;


import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Memory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String memoryName; //nombre del recuerdo(album virtual)
    private String description;// descripcion del recuerdo
    private LocalDateTime memoryCreationDate; //Fecha de creacion del recuerdo

    //relacion Many-to-One con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //usuario creador del recuerdo

    //relacion One-to-Many con publicaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publication> publications = new ArrayList<>(); // Lista de publicaciones asociadas al recuerdo

    // Relación Many-to-Many con eventos
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "memory_event",
            joinColumns = @JoinColumn(name = "memory_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>(); // Lista de eventos asociados al recuerdo
}
