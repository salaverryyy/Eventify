package com.eventos.recuerdos.eventify_project.memory.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceBadRequestException;
import com.eventos.recuerdos.eventify_project.memory.domain.MemoryService;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryEventDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/memories")
public class MemoryController {

    @Autowired
    private MemoryService memoryService;

    @GetMapping("/{id}")
    public ResponseEntity<MemoryDTO> getMemoryById(@PathVariable Long id) {
        MemoryDTO memoryDTO = memoryService.getMemoryById(id);
        return ResponseEntity.ok(memoryDTO);
    }

    @PostMapping
    public ResponseEntity<MemoryDTO> createMemory(
            @RequestParam("memoryName") String memoryName,
            @RequestParam("description") String description,
            @RequestParam("coverPhoto") MultipartFile coverPhoto,
            Principal principal) {

        // Crear el DTO y asignar los valores
        MemoryDTO memoryDTO = new MemoryDTO();
        memoryDTO.setMemoryName(memoryName);
        memoryDTO.setDescription(description);

        // Llamar al servicio para crear el Memory, pasando el Principal y la foto
        MemoryDTO createdMemory = memoryService.createMemory(memoryDTO, coverPhoto, principal);

        // Devolver la respuesta HTTP con el Memory creado
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMemory);
    }






    @PutMapping("/{id}")
    public ResponseEntity<MemoryDTO> updateMemory(@PathVariable Long id, @Valid @RequestBody MemoryDTO memoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResourceBadRequestException("pon los datos correctos porfavor");
        }
        MemoryDTO updatedMemory = memoryService.updateMemory(id, memoryDTO);
        return ResponseEntity.ok(updatedMemory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/publicaciones")
    public ResponseEntity<MemoryWithPublicationsDTO> getMemoryWithPublications(@PathVariable Long id) {
        MemoryWithPublicationsDTO memoryWithPublications = memoryService.getMemoryWithPublications(id);
        return ResponseEntity.ok(memoryWithPublications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MemoryEventDto>> getMemoriesForUser(@PathVariable Long userId) {
        List<MemoryEventDto> memories = memoryService.getMemoriesForUser(userId);
        if (memories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(memories);
    }

    //Obtener todos los memory credos
    @GetMapping
    public ResponseEntity<List<MemoryDTO>> getAllMemories() {
        List<MemoryDTO> memories = memoryService.getAllMemories();
        return ResponseEntity.ok(memories);
    }
}