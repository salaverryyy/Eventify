package com.eventos.recuerdos.eventify_project.memory.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceBadRequestException;
import com.eventos.recuerdos.eventify_project.memory.domain.MemoryService;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryEventDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryWithPublicationsDTO;
import com.eventos.recuerdos.eventify_project.controllers.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private StorageService storageService;

    // Crear un nuevo recuerdo con una foto de portada
    @PostMapping
    public ResponseEntity<?> createMemory(
            @RequestParam("memoryName") String memoryName,
            @RequestParam("description") String description,
            @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto,
            Principal principal) {

        // Validar campos obligatorios
        if (memoryName == null || memoryName.isBlank() || description == null || description.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre y la descripción son obligatorios.");
        }

        try {
            // Crear el DTO para el nuevo recuerdo
            MemoryDTO memoryDTO = new MemoryDTO();
            memoryDTO.setMemoryName(memoryName);
            memoryDTO.setDescription(description);

            // Subir la foto de portada, si existe
            if (coverPhoto != null && !coverPhoto.isEmpty()) {
                String coverPhotoKey = "cover-pics/" + principal.getName() + "/" + coverPhoto.getOriginalFilename();
                String uploadedPhotoKey = storageService.uploadFile(coverPhoto, coverPhotoKey);
                memoryDTO.setCoverPhotoKey(uploadedPhotoKey);
            }

            // Crear el recuerdo en la base de datos
            MemoryDTO createdMemory = memoryService.createMemory(memoryDTO, coverPhoto, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMemory);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el recuerdo: " + e.getMessage());
        }
    }

    // Obtener la foto de portada de un recuerdo
    @GetMapping("/{id}/cover-photo")
    public ResponseEntity<String> getCoverPhoto(@PathVariable Long id) {
        try {
            String coverPhotoKey = memoryService.getCoverPhotoKey(id);
            String presignedUrl = storageService.generatePresignedUrl(coverPhotoKey);
            return ResponseEntity.ok(presignedUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la foto de portada.");
        }
    }

    // Eliminar la foto de portada de un recuerdo
    @DeleteMapping("/{id}/cover-photo")
    public ResponseEntity<String> deleteCoverPhoto(@PathVariable Long id) {
        try {
            String coverPhotoKey = memoryService.getCoverPhotoKey(id);
            storageService.deleteFile(coverPhotoKey); // Eliminar de S3
            memoryService.removeCoverPhotoKey(id);    // Eliminar referencia en la base de datos
            return ResponseEntity.ok("Foto de portada eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la foto de portada: " + e.getMessage());
        }
    }

    // Obtener un recuerdo por ID
    @GetMapping("/{id}")
    public ResponseEntity<MemoryDTO> getMemoryById(@PathVariable Long id) {
        MemoryDTO memoryDTO = memoryService.getMemoryById(id);
        return ResponseEntity.ok(memoryDTO);
    }

    // Actualizar un recuerdo
    @PutMapping("/{id}")
    public ResponseEntity<MemoryDTO> updateMemory(@PathVariable Long id, @Valid @RequestBody MemoryDTO memoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResourceBadRequestException("Por favor, ingresa los datos correctos.");
        }
        MemoryDTO updatedMemory = memoryService.updateMemory(id, memoryDTO);
        return ResponseEntity.ok(updatedMemory);
    }

    // Eliminar un recuerdo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener las publicaciones de un recuerdo
    @GetMapping("/{id}/publicaciones")
    public ResponseEntity<MemoryWithPublicationsDTO> getMemoryWithPublications(@PathVariable Long id) {
        MemoryWithPublicationsDTO memoryWithPublications = memoryService.getMemoryWithPublications(id);
        return ResponseEntity.ok(memoryWithPublications);
    }

    // Obtener recuerdos de un usuario específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MemoryEventDto>> getMemoriesForUser(@PathVariable Long userId) {
        List<MemoryEventDto> memories = memoryService.getMemoriesForUser(userId);
        if (memories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(memories);
    }

    // Obtener todos los recuerdos
    @GetMapping
    public ResponseEntity<List<MemoryDTO>> getAllMemories() {
        List<MemoryDTO> memories = memoryService.getAllMemories();
        return ResponseEntity.ok(memories);
    }
}

