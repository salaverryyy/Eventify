package com.eventos.recuerdos.eventify_project.like.application;

import com.eventos.recuerdos.eventify_project.like.domain.LikeService;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likePublication")
public class LikeController {
    @Autowired
    private LikeService likeService;

    // Dar "me gusta" a una publicación
    @PostMapping("/{publicationId}/like")
    public ResponseEntity<String> likePublication(@PathVariable Long publicationId) {
        likeService.likePublication(publicationId);
        return ResponseEntity.ok("Like agregado exitosamente.");
    }

    // Obtener la lista de usuarios que han dado "me gusta" a una publicación
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserDTO>> getUsersWhoLikedPublication(@PathVariable Long id) {
        List<UserDTO> users = likeService.getUsersWhoLikedPublication(id);
        return ResponseEntity.ok(users);
    }

    // Quitar "me gusta" de una publicación
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> unlikePublication(@PathVariable Long id, @RequestParam Long userId) {
        likeService.unlikePublication(id, userId);
        return ResponseEntity.noContent().build();
    }

    //obtener todos los likes dados
    @GetMapping
    public ResponseEntity<List<LikeDTO>> getAllLikes() {
        List<LikeDTO> likes = likeService.getAllLikes();
        return ResponseEntity.ok(likes);
    }
}
