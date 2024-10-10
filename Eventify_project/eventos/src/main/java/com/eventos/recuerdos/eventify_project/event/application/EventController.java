package com.eventos.recuerdos.eventify_project.event.application;

import com.eventos.recuerdos.eventify_project.event.domain.EventService;
import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.user.dto.EventGuestDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
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

    @Autowired
    InvitationService invitationService;


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

    // Obtener el recuerdo asociado a un evento
    @GetMapping("/{id}/memory")
    public ResponseEntity<MemoryDTO> getEventMemory(@PathVariable Long id) {
        MemoryDTO memoryDTO = eventService.getEventMemory(id);
        return ResponseEntity.ok(memoryDTO);
    }


    // Obtener lista de usuarios invitados a un evento
    @GetMapping("/{eventId}/invitados")
    public List<EventGuestDTO> getEventGuests(@PathVariable Long eventId) {
        return eventService.getEventGuests(eventId);
    }

    // Endpoint para asociar un Memory a un Event
    @PostMapping("/{eventId}/memory/{memoryId}")
    public ResponseEntity<EventDTO> addMemoryToEvent(@PathVariable Long eventId, @PathVariable Long memoryId) {
        EventDTO updatedEvent = eventService.addMemoryToEvent(eventId, memoryId);
        return ResponseEntity.ok(updatedEvent);
    }

    //Obtener todos los Eventos creados
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
