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
    @PostMapping(value = "/{memoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PublicationDTO> createPublication(
            @PathVariable Long memoryId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("userId") Long userId) {

        // Llamamos al servicio para crear la publicación
        PublicationDTO createdPublication = publicationService.createPublication(memoryId, file, description, userId);

        // Retornamos una respuesta con el estado creado (201)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
    }


    //Editar la descripción o archivo de una publicación
    @PutMapping("/{id}")
    public ResponseEntity<PublicationDTO> updatePublication(@PathVariable Long id, @RequestBody PublicationDTO publicationDTO) {
        PublicationDTO updatedPublication = publicationService.updatePublication(id, publicationDTO);
        return ResponseEntity.ok(updatedPublication);
    }

    //Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    //Dar "me gusta" a una publicación
    @PostMapping("/{id}/likes")
    public ResponseEntity<LikeDTO> likePublication(@PathVariable Long id, @RequestBody LikeDTO likeDTO) {
        LikeDTO likedPublication = publicationService.likePublication(id, likeDTO);
        return ResponseEntity.ok(likedPublication);
    }

    //Comentar en una publicación
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<CommentDTO> commentOnPublication(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = publicationService.commentOnPublication(id, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    //Obtener la lista de usuarios que dieron "me gusta" a una publicación
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<LikeDTO>> getLikesByPublication(@PathVariable Long id) {
        List<LikeDTO> likes = publicationService.getLikesByPublication(id);
        return ResponseEntity.ok(likes);
    }

    //Quitar "me gusta" de una publicación
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @RequestBody LikeDTO likeDTO) {
        publicationService.removeLike(id, likeDTO);
        return ResponseEntity.noContent().build();
    }





}
