package com.eventos.recuerdos.eventify_project.event.domain;

import com.eventos.recuerdos.eventify_project.event.dto.EventDTO;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
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
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));

        modelMapper.map(eventDTO, event); // Actualiza los detalles del evento
        Event updatedEvent = eventRepository.save(event);
        return modelMapper.map(updatedEvent, EventDTO.class);
    }

    // Eliminar un evento
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        eventRepository.delete(event);
    }

    // Obtener todos los recuerdos asociados a un evento
    public List<MemoryDTO> getEventMemories(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        return event.getMemories().stream()
                .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                .collect(Collectors.toList());
    }

    // Obtener lista de invitados del evento
    public List<InvitationDTO> getEventInvitations(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + id));
        return event.getInvitations().stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDTO.class))
                .collect(Collectors.toList());
    }



    //Obtener todos los eventos creados
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }
}
