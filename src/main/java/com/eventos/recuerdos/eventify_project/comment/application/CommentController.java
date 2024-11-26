package com.eventos.recuerdos.eventify_project.comment.application;

import com.eventos.recuerdos.eventify_project.comment.domain.CommentService;
import com.eventos.recuerdos.eventify_project.comment.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // Obtener los detalles de un comentario por ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // Crear un nuevo comentario en una publicación
    @PostMapping("/publicaciones/{publicationId}/comentarios")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long publicationId,
            @RequestBody CommentDTO commentDTO,
            Principal principal) {
        CommentDTO createdComment = commentService.createComment(publicationId, commentDTO, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Editar el contenido de un comentario por ID
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // Eliminar un comentario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los comentarios de una publicación
    @GetMapping("/publicaciones/{publicationId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPublicationId(@PathVariable Long publicationId) {
        List<CommentDTO> comments = commentService.getCommentsByPublicationId(publicationId);
        return ResponseEntity.ok(comments);
    }

    // Obtener todos los comentarios creados
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
