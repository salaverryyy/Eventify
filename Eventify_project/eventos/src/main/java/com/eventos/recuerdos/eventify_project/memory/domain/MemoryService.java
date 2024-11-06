package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return modelMapper.map(memory, MemoryDTO.class);
    }

    public MemoryDTO createMemory(MemoryDTO memoryDTO, Principal principal) {
        String userEmail = principal.getName();
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);

        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        if (memoryRepository.existsByMemoryName(memoryDTO.getMemoryName())) {
            throw new ResourceConflictException("Ya existe un recuerdo con el mismo título.");
        }

        Memory memory = modelMapper.map(memoryDTO, Memory.class);
        memory.setUserAccount(userAccount);
        memory.setMemoryCreationDate(LocalDateTime.now());
        memory.setAccessCode(generateAccessCode()); // Asignar código de acceso único
        Memory savedMemory = memoryRepository.save(memory);

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

    public List<MemoryDTO> getAllMemories() {
        List<Memory> memories = memoryRepository.findAll();
        return memories.stream()
                .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                .collect(Collectors.toList());
    }
}
