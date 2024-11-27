package com.eventos.recuerdos.eventify_project.like.application;

import com.eventos.recuerdos.eventify_project.like.domain.LikeService;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/likePublication")
public class LikeController {
    @Autowired
    private LikeService likeService;

    // Dar "me gusta" a una publicación
    @PostMapping("/{publicationId}/like")
    public ResponseEntity<String> likePublication(@PathVariable Long publicationId, Principal principal) {
        likeService.likePublication(publicationId, principal.getName());
        return ResponseEntity.ok("Like agregado exitosamente.");
    }

    // Quitar "me gusta" de una publicación
    @PostMapping("/{publicationId}/unlike")
    public ResponseEntity<String> unlikePublication(@PathVariable Long publicationId, Principal principal) {
        likeService.unlikePublication(publicationId, principal.getName());
        return ResponseEntity.ok("Like eliminado exitosamente.");
    }


    // Obtener la lista de usuarios que han dado "me gusta" a una publicación
    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserDTO>> getUsersWhoLikedPublication(@PathVariable Long id) {
        List<UserDTO> users = likeService.getUsersWhoLikedPublication(id);
        return ResponseEntity.ok(users);
    }

    // Obtener todos los likes dados
    @GetMapping
    public ResponseEntity<List<LikeDTO>> getAllLikes() {
        List<LikeDTO> likes = likeService.getAllLikes();
        return ResponseEntity.ok(likes);
    }


}
