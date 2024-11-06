package com.eventos.recuerdos.eventify_project.publication.application;

import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.publication.domain.PublicationService;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationCreationResponseDto;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    // Obtener los detalles de una publicación por su ID
    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublicationById(@PathVariable Long id) {
        PublicationDTO publicationDTO = publicationService.getPublicationById(id);
        return ResponseEntity.ok(publicationDTO);
    }

    // Subir una nueva publicación a un recuerdo
    @PostMapping("/{memoryId}")
    public ResponseEntity<PublicationCreationResponseDto> createPublication(
            @PathVariable Long memoryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            Principal principal) {

        // Llamamos al servicio para crear la publicación y pasamos el email del usuario del token
        PublicationCreationResponseDto createdPublication = publicationService.createPublication(memoryId, file, description, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
    }

    // Editar la descripción o archivo de una publicación
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicationCreationResponseDto> updatePublication(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            Principal principal) {

        // Llamar al servicio para actualizar la publicación y obtener el email del usuario del token
        PublicationCreationResponseDto updatedPublication = publicationService.updatePublication(id, file, description, principal.getName());
        return ResponseEntity.ok(updatedPublication);
    }

    // Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener la lista de usuarios que dieron "me gusta" a una publicación
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<LikeDTO>> getLikesByPublication(@PathVariable Long id) {
        List<LikeDTO> likes = publicationService.getLikesByPublication(id);
        return ResponseEntity.ok(likes);
    }
}
