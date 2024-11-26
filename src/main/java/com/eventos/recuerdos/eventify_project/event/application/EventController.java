package com.eventos.recuerdos.eventify_project.event.application;

import com.eventos.recuerdos.eventify_project.event.domain.EventService;
import com.eventos.recuerdos.eventify_project.event.dto.EventBasicDto;
import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.user.dto.EventGuestDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    InvitationService invitationService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private static final Logger log = LoggerFactory.getLogger(EventController.class);



    //Obtener los detalles de un evento
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    //Crear un nuevo evento
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventBasicDto> createEvent(@RequestBody EventDTO eventDTO, Principal principal) {
        // Extraer el ID del usuario desde el token (por medio del email o nombre del usuario en `principal`)
        String email = principal.getName();  // Esto obtiene el email del usuario autenticado
        EventBasicDto createdEvent = eventService.createEvent(eventDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }


    //Actualizar los detalles del evento
    @PatchMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO, Principal principal) {
        // Extrae el organizerId a partir del token
        Long organizerId = eventService.extractUserIdFromToken(principal);
        // Llama a updateEvent en el servicio pasando el organizerId extraído
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO, organizerId);
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


    // Endpoint para asociar un Memory a un Event
    @PostMapping("/{eventId}/memory/{memoryId}")
    public ResponseEntity<EventDTO> addMemoryToEvent(@PathVariable Long eventId, @PathVariable Long memoryId) {
        EventDTO updatedEvent = eventService.addMemoryToEvent(eventId, memoryId);
        log.debug("Asociando memory {} al event {}", memoryId, eventId);
        return ResponseEntity.ok(updatedEvent);
    }

    //Obtener todos los Eventos creados
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
