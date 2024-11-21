package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.event.dto.EventBasicDto;
import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryEventDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import com.eventos.recuerdos.eventify_project.controllers.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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

    @Autowired
    private StorageService storageService;  // Inyección de StorageService para manejar S3

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
        Memory memory = memoryRepository.findById(id).orElse(null);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id);
        }
        return modelMapper.map(memory, MemoryDTO.class);
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

        // Crear la entidad Memory sin el albumLink
        Memory memory = modelMapper.map(memoryDTO, Memory.class);
        memory.setUserAccount(userAccount);
        memory.setMemoryCreationDate(LocalDateTime.now());
        memory.setAccessCode(generateAccessCode());

        // Subir la foto de portada a S3 y almacenar su clave
        String coverPhotoKey = "cover-pics/" + memory.getId() + "-" + coverPhoto.getOriginalFilename();
        try {
            String storedPhotoKey = storageService.uploadFile(coverPhoto, coverPhotoKey);
            memory.setCoverPhotoKey(storedPhotoKey);  // Almacenar la clave de la foto de portada en S3
        } catch (Exception e) {
            throw new RuntimeException("Error al subir la foto de portada", e);
        }

        // Agregar el usuario creador a la lista de participantes
        memory.getParticipants().add(userAccount);

        // Guardar inicialmente el Memory sin el albumLink
        Memory savedMemory = memoryRepository.save(memory);

        // Ahora genera el albumLink usando el memoryId
        String albumLink = "http://localhost:5173/album/" + savedMemory.getId();
        savedMemory.setAlbumLink(albumLink);

        // Guardar nuevamente el Memory con el albumLink actualizado
        savedMemory = memoryRepository.save(savedMemory);

        return modelMapper.map(savedMemory, MemoryDTO.class);
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
        List<Memory> userMemories = memoryRepository.findMemoriesByParticipantsId(userId);
        return userMemories.stream()
                .map(this::convertToMemoryEventDto)
                .collect(Collectors.toList());
    }

    private MemoryEventDto convertToMemoryEventDto(Memory memory) {
        MemoryEventDto dto = new MemoryEventDto();
        dto.setMemoryId(memory.getId());
        dto.setCoverPhoto(memory.getCoverPhotoKey());

        if (memory.getEvent() != null) {
            EventBasicDto eventDto = new EventBasicDto();
            eventDto.setEventId(memory.getEvent().getId());
            eventDto.setEventName(memory.getEvent().getEventName());
            eventDto.setEventDate(memory.getEvent().getEventDate());
            dto.setEvent(eventDto);
        }

        return dto;
    }

    public List<MemoryDTO> getAllMemories() {
        List<Memory> memories = memoryRepository.findAll();
        return memories.stream()
                .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                .collect(Collectors.toList());
    }

    // Nuevo método: Obtener clave de la foto de portada
    public String getCoverPhotoKey(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId).orElse(null);
        if (memory == null || memory.getCoverPhotoKey() == null) {
            throw new ResourceNotFoundException("No se encontró la foto de portada para el recuerdo con ID: " + memoryId);
        }
        return memory.getCoverPhotoKey();
    }

    // Nuevo método: Actualizar la clave de la foto de portada
    public void updateCoverPhotoKey(Long memoryId, String fileKey) {
        Memory memory = memoryRepository.findById(memoryId).orElse(null);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + memoryId);
        }
        memory.setCoverPhotoKey(fileKey);
        memoryRepository.save(memory);
    }

    // Nuevo método: Eliminar la referencia de la foto de portada
    public void removeCoverPhotoKey(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId).orElse(null);
        if (memory == null) {
            throw new ResourceNotFoundException("No se encontró el recuerdo con ID: " + memoryId);
        }
        memory.setCoverPhotoKey(null);
        memoryRepository.save(memory);
    }
}

