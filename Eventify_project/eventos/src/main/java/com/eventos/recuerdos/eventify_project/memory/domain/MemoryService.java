package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceBadRequestException;
import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemoryService {
    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MemoryDTO getMemoryById(Long id) {
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id));
        return modelMapper.map(memory, MemoryDTO.class);
    }

    public MemoryDTO createMemory(MemoryDTO memoryDTO) {
        // Verificar si ya existe un recuerdo con el mismo nombre
        if (memoryRepository.existsByMemoryName(memoryDTO.getMemoryName())) {
            throw new ResourceConflictException("Ya existe un recuerdo con el mismo título.");
        }

        // Buscar al usuario por el userId que viene en el DTO
        User user = userRepository.findById(memoryDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + memoryDTO.getUserId()));

        // Mapear el DTO a la entidad Memory
        Memory memory = modelMapper.map(memoryDTO, Memory.class);

        // Asignar el usuario a la entidad Memory
        memory.setUser(user);

        // Guardar el Memory en la base de datos
        Memory savedMemory = memoryRepository.save(memory);

        // Retornar el DTO del Memory guardado
        return modelMapper.map(savedMemory, MemoryDTO.class);
    }


    public MemoryDTO updateMemory(Long id, MemoryDTO memoryDTO) {
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id));
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
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recuerdo con ID: " + id));
        List<PublicationDTO> publications = publicationRepository.findByMemoryId(id)
                .stream()
                .map(p -> modelMapper.map(p, PublicationDTO.class))
                .collect(Collectors.toList());
        return new MemoryWithPublicationsDTO(modelMapper.map(memory, MemoryDTO.class), publications);
    }
}
