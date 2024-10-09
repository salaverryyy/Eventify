package com.eventos.recuerdos.eventify_project.like.application;

import com.eventos.recuerdos.eventify_project.like.domain.LikeService;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    // Dar "me gusta" a una publicación
    @PostMapping("/{id}/likes")
    public ResponseEntity<?> likePublication(@PathVariable Long id, @RequestParam Long userId) {
        likeService.likePublication(id, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
}
