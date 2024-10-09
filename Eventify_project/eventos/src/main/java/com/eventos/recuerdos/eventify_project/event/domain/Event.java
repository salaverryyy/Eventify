package com.eventos.recuerdos.eventify_project.event.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;

    // Relación One-to-One con Memory
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", unique = true)  // Añade 'unique = true' para garantizar que solo un Event esté relacionado con un Memory
    private Memory memory;  // Ahora un Event solo puede tener un Memory asociado.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer; // Usuario que organizó el evento

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invitation> invitations = new ArrayList<>(); // Lista de invitaciones del evento
}
