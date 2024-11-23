package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.event.dto.EventBasicDto;
import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryEventDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemoryService {
    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;

    private String generateAccessCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        while (code.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }


    public MemoryDTO getMemoryById(Long id) {
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id));

        // Convertir Memory a MemoryDTO usando ModelMapper o creando manualmente un DTO
        return modelMapper.map(memory, MemoryDTO.class);
    }

    public Memory getMemoryEntityById(Long id) {
        return memoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id));
    }

    public MemoryDTO createMemory(MemoryDTO memoryDTO, MultipartFile coverPhoto, Principal principal) {
        String userEmail = principal.getName();
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);

        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        if (memoryRepository.existsByMemoryName(memoryDTO.getMemoryName())) {
            throw new ResourceConflictException("Ya existe un recuerdo con el mismo título.");
        }

        // Crear la entidad Memory
        Memory memory = modelMapper.map(memoryDTO, Memory.class);
        memory.setUserAccount(userAccount);
        memory.setMemoryCreationDate(LocalDateTime.now());
        memory.setAccessCode(generateAccessCode()); // Asignar código de acceso único

        // Generar y asignar el albumLink basado en un UUID
        String albumLink = generateAlbumLinkWithUUID();
        memory.setAlbumLink(albumLink);

        // Agregar al creador como participante
        memory.getParticipants().add(userAccount);

        // Guardar el Memory con el albumLink y participantes
        Memory savedMemory = memoryRepository.save(memory);

        return modelMapper.map(savedMemory, MemoryDTO.class);
    }


    // Método para generar el albumLink usando UUID
    private String generateAlbumLinkWithUUID() {
        return "http://localhost:5173/album/" + UUID.randomUUID().toString();
    }


    public MemoryDTO updateMemory(Long id, MemoryDTO memoryDTO) {
        Memory memory = memoryRepository.findById(id).orElse(null);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id);
        }
        memory.setMemoryName(memoryDTO.getMemoryName());
        memory.setDescription(memoryDTO.getDescription());
        memoryRepository.save(memory);
        return modelMapper.map(memory, MemoryDTO.class);
    }

    public void deleteMemory(Long id) {
        if (!memoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id);
        }
        memoryRepository.deleteById(id);
    }

    public MemoryWithPublicationsDTO getMemoryWithPublications(Long id) {
        Memory memory = memoryRepository.findById(id).orElse(null);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id);
        }
        List<PublicationDTO> publications = publicationRepository.findByMemoryId(id)
                .stream()
                .map(p -> modelMapper.map(p, PublicationDTO.class))
                .collect(Collectors.toList());
        return new MemoryWithPublicationsDTO(modelMapper.map(memory, MemoryDTO.class), publications);
    }

    public List<MemoryEventDto> getMemoriesForUser(Long userId) {
        // Obtener el usuario por su ID
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Obtener recuerdos creados por el usuario
        List<Memory> createdMemories = memoryRepository.findByUserAccountId(userId);

        // Obtener recuerdos en los cuales el usuario ha sido invitado y aceptado usando la nueva lista de acceptedInvitations,
        // excluyendo los que el usuario haya creado.
        List<Memory> invitedMemories = user.getAcceptedInvitations().stream()
                .map(Invitation::getMemory)
                .filter(memory -> !memory.getUserAccount().getId().equals(userId))
                .collect(Collectors.toList());

        // Combinar las listas en una única lista
        List<Memory> allMemories = new ArrayList<>();
        allMemories.addAll(createdMemories);
        allMemories.addAll(invitedMemories);

        // Convertir a MemoryEventDto
        return allMemories.stream()
                .map(this::convertToMemoryEventDto)
                .collect(Collectors.toList());
    }



    private MemoryEventDto convertToMemoryEventDto(Memory memory) {
        MemoryEventDto dto = new MemoryEventDto();
        dto.setMemoryId(memory.getId());
        dto.setCoverPhoto(memory.getCoverPhoto());

        // Convertir el evento si existe
        if (memory.getEvent() != null) {
            EventBasicDto eventDto = new EventBasicDto();
            eventDto.setEventId(memory.getEvent().getId());
            eventDto.setEventName(memory.getEvent().getEventName());
            eventDto.setEventDate(memory.getEvent().getEventDate());
            dto.setEvent(eventDto);
        }

        return dto;
    }

    public Memory getMemoryByUUID(String uuid) {
        Memory memory = memoryRepository.findByAlbumLinkContaining(uuid);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con UUID: " + uuid);
        }
        return memory;
    }


    public List<MemoryDTO> getAllMemories() {
        List<Memory> memories = memoryRepository.findAll();
        return memories.stream()
                .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                .collect(Collectors.toList());
    }
}
