package com.eventos.recuerdos.eventify_project.event.application;

import com.eventos.recuerdos.eventify_project.event.domain.EventService;
import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;

    //Obtener los detalles de un evento
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    //Crear un nuevo evento
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    //Actualizar los detalles del evento
    @PatchMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    //Eliminar un evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    //Obtener todos los recuerdos asociados a un evento
    @GetMapping("/{id}/recuerdos")
    public ResponseEntity<List<MemoryDTO>> getEventMemories(@PathVariable Long id) {
        List<MemoryDTO> memories = eventService.getEventMemories(id);
        return ResponseEntity.ok(memories);
    }

    //Obtener la lista de invitados del evento
    @GetMapping("/{id}/invitados")
    public ResponseEntity<List<InvitationDTO>> getEventInvitations(@PathVariable Long id) {
        List<InvitationDTO> invitations = eventService.getEventInvitations(id);
        return ResponseEntity.ok(invitations);
    }

    //Obtener todos los Eventos creados
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
