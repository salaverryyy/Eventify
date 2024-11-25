package com.eventos.recuerdos.eventify_project.event.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String location;

    // Usuario organizador del evento
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount organizer;  // Asegúrate de usar 'organizer' en el repositorio

    // Relación One-to-One con Memory
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", unique = true)
    private Memory memory;

}