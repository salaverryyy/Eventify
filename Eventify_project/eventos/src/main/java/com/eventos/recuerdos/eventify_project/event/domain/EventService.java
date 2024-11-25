package com.eventos.recuerdos.eventify_project.event.domain;

import com.eventos.recuerdos.eventify_project.event.dto.EventBasicDto;
import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.dto.EventGuestDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.springframework.security.access.AccessDeniedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Obtener evento por ID
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        return modelMapper.map(event, EventDTO.class);
    }

    // Crear un nuevo evento
    public EventBasicDto createEvent(EventDTO eventDTO, String email) {
        // Buscar el usuario por email
        UserAccount organizer = userAccountRepository.findByEmail(email);
        if (organizer == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con el email: " + email);
        }

        // Mapear el DTO a la entidad Event
        Event event = modelMapper.map(eventDTO, Event.class);
        event.setOrganizer(organizer);
        event.setLocation(eventDTO.getLocation());

        // Guardar el evento y mapearlo a EventBasicDTO
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventBasicDto.class);
    }

    // Actualizar un evento existente
    public EventDTO updateEvent(Long id, EventDTO eventDTO, Long organizerId) {
        // Busca el evento que deseas actualizar
        Event event = eventRepository.findEventById(id);
        if (event == null) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + id);
        }

        // Verifica si el organizerId coincide con el organizador del evento
        if (event.getOrganizer() == null || !event.getOrganizer().getId().equals(organizerId)) {
            throw new AccessDeniedException("No tienes permiso para actualizar este evento.");
        }

        // Actualiza los campos del evento, excepto el ID y el organizerId
        event.setEventName(eventDTO.getEventName());
        event.setEventDescription(eventDTO.getEventDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setLocation(eventDTO.getLocation());

        // Guarda el evento actualizado
        Event updatedEvent = eventRepository.save(event);

        // Retorna el DTO del evento actualizado
        return modelMapper.map(updatedEvent, EventDTO.class);
    }

    public Long extractUserIdFromToken(Principal principal) {
        UserAccount user = userAccountRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        return user.getId();
    }




    // Eliminar un evento
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        eventRepository.delete(event);
    }

    // Obtener el recuerdo asociado a un evento
    public MemoryDTO getEventMemory(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));

        Memory memory = event.getMemory();
        if (memory == null) {
            throw new ResourceNotFoundException("No hay recuerdo asociado con el evento con id: " + id);
        }

        return modelMapper.map(memory, MemoryDTO.class);
    }




    // Método para agregar un Memory a un Event
    public EventDTO addMemoryToEvent(Long eventId, Long memoryId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + eventId));

        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Recuerdo no encontrado con id: " + memoryId));

        event.setMemory(memory);  // Asociamos el Memory al Event
        eventRepository.save(event);  // Guardamos el evento actualizado

        // Mapear el Event a EventDTO, incluyendo el MemoryDTO
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        eventDTO.setMemory(modelMapper.map(memory, MemoryDTO.class));  // Incluye el MemoryDTO
        return eventDTO;
    }


    //Obtener todos los eventos creados
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }
}
