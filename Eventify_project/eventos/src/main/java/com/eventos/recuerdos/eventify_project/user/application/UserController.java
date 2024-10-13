package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userProfile(Principal principal) {
        return ResponseEntity.ok("Perfil de: " + principal.getName());
    }


    //busqueda de usuarios
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userService.searchByUsername(username);
        return ResponseEntity.ok(users);
    }



    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO); // Retorna 200 OK con el cuerpo del usuario
    }


    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser); // Retorna 200 OK con el cuerpo del usuario actualizado
    }

    //Eliminar usuario
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?>deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    //obtener los albums donde participa el usuario
    @GetMapping("/{id}/recuerdos")
    public ResponseEntity<List<MemoryDTO>> getUserMemories(@PathVariable Long id) {
        List<MemoryDTO> memories = userService.getUserMemories(id);
        return ResponseEntity.ok(memories);
    }

    //obtener las invitaciones de un usuario en especifico
    @GetMapping("/{id}/invitaciones")
    public ResponseEntity<List<InvitationDTO>> getUserInvitations(@PathVariable Long id) {
        List<InvitationDTO> invitations = userService.getUserInvitations(id);
        return ResponseEntity.ok(invitations);
    }

    //obtener las notificaciones de un usuario en especifico
    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDTO> notifications = userService.getUserNotifications(id);
        return ResponseEntity.ok(notifications);
    }

}
