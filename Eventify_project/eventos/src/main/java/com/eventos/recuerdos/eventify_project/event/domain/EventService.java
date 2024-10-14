package com.eventos.recuerdos.eventify_project.event.domain;

import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.dto.EventGuestDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

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
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDTO.class);
    }

    // Actualizar un evento existente
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        // Busca el evento que deseas actualizar
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + id));

        // Actualiza los campos del evento, excepto el ID
        event.setEventName(eventDTO.getEventName());
        event.setEventDescription(eventDTO.getEventDescription());
        event.setEventDate(eventDTO.getEventDate());

        // Actualizar el organizador solo si se proporciona un organizerId válido
        if (eventDTO.getOrganizerId() != null) {
            User organizer = userRepository.findById(eventDTO.getOrganizerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organizador no encontrado con ID: " + eventDTO.getOrganizerId()));
            event.setOrganizer(organizer);
        }

        // Guarda el evento actualizado
        Event updatedEvent = eventRepository.save(event);

        // Retorna el DTO del evento actualizado
        return modelMapper.map(updatedEvent, EventDTO.class);
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


    // Obtener lista de invitados del evento
    public List<EventGuestDTO> getEventGuests(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + eventId));

        // Obtener las invitaciones del evento y los usuarios invitados
        return event.getInvitations().stream()
                .map(Invitation::getUsuarioInvitado)
                .filter(user -> user != null) // Asegurarse de que no haya invitados nulos
                .map(user -> modelMapper.map(user, EventGuestDTO.class))
                .collect(Collectors.toList());
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
