package com.eventos.recuerdos.eventify_project.publication.application;


import com.eventos.recuerdos.eventify_project.comment.dto.CommentDTO;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.publication.domain.PublicationService;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    //Obtener los detalles de una publicación por su ID
    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublicationById(@PathVariable Long id) {
        PublicationDTO publicationDTO = publicationService.getPublicationById(id);
        return ResponseEntity.ok(publicationDTO);
    }

    // Subir una nueva publicación a un recuerdo
    @PostMapping("/{memoryId}")
    public ResponseEntity<PublicationDTO> createPublication(
            @PathVariable Long memoryId,

            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("userId") Long userId) {

        // Llamamos al servicio para crear la publicación
        PublicationDTO createdPublication = publicationService.createPublication(memoryId, file, description, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
    }


    //Editar la descripción o archivo de una publicación
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicationDTO> updatePublication(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {

        // Llamar al servicio para actualizar la publicación
        PublicationDTO updatedPublication = publicationService.updatePublication(id, file, description);

        // Retornar la respuesta con la publicación actualizada
        return ResponseEntity.ok(updatedPublication);
    }


    //Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }


    //Obtener la lista de usuarios que dieron "me gusta" a una publicación
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<LikeDTO>> getLikesByPublication(@PathVariable Long id) {
        List<LikeDTO> likes = publicationService.getLikesByPublication(id);
        return ResponseEntity.ok(likes);
    }


}
