package com.eventos.recuerdos.eventify_project.publication.application;

import com.eventos.recuerdos.eventify_project.controllers.StorageService;
import com.eventos.recuerdos.eventify_project.like.domain.LikeService;
import com.eventos.recuerdos.eventify_project.like.dto.LikeCountDto;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.domain.PublicationService;
import com.eventos.recuerdos.eventify_project.publication.dto.DetailedPublicationDTO;
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
    private StorageService storageService;
    @Autowired
    private PublicationService publicationService;

    @Autowired
    private LikeService likeService;

    // Obtener los detalles de una publicación por su ID
    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublicationById(@PathVariable Long id) {
        PublicationDTO publicationDTO = publicationService.getPublicationById(id);
        return ResponseEntity.ok(publicationDTO);
    }
    @GetMapping("/{id}/publication")
    public ResponseEntity<String> getPublicationPhoto(@PathVariable Long id) {
        PublicationDTO publicationDTO = publicationService.getPublicationById(id);
        if (publicationDTO.getFileUrl() == null)
            return ResponseEntity.notFound().build();
        String presignedUrl = storageService.generatePresignedUrl(publicationDTO.getFileUrl());
        return ResponseEntity.ok(presignedUrl);
    }

    // Subir una nueva publicación a un recuerdo
    @PostMapping("/{memoryId}")
    public ResponseEntity<PublicationCreationResponseDto> createPublication(
            @PathVariable Long memoryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            Principal principal) throws Exception {
        String objectKey = "publication-pics/" + file.getOriginalFilename();
        String fileKey = storageService.uploadFile(file, objectKey);
        // Llamamos al servicio para crear la publicación y pasamos el email del usuario del token
        PublicationCreationResponseDto createdPublication = publicationService.createPublication(memoryId,file, fileKey, description, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
    }

    // Editar la descripción o archivo de una publicación
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicationCreationResponseDto> updatePublication(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            Principal principal) throws Exception {

        String objectKey = "publication-pics/" + file.getOriginalFilename();
        String fileKey = storageService.uploadFile(file, objectKey);

        // Llamar al servicio para actualizar la publicación y obtener el email del usuario del token
        PublicationCreationResponseDto updatedPublication = publicationService.updatePublication(id, file,fileKey, description, principal.getName());
        return ResponseEntity.ok(updatedPublication);
    }

    // Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener la cantidad de "me gusta" en una publicación
    @GetMapping("/{publicationId}/likes/count")
    public ResponseEntity<LikeCountDto> getLikesByPublication(@PathVariable Long publicationId) {
        int likeCount = likeService.getLikeCountByPublication(publicationId);
        LikeCountDto likeCountDto = new LikeCountDto(likeCount);
        return ResponseEntity.ok(likeCountDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DetailedPublicationDTO>> getPublicationsForAuthor(@PathVariable Long userId) {
        List<DetailedPublicationDTO> publications = publicationService.getPublicationsForAuthor(userId);
        if (publications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publications);
    }
}
