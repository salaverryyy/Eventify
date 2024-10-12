package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.service.UserService;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO); // Retorna 200 OK con el cuerpo del usuario
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // Retorna 201 Created con el cuerpo del nuevo usuario
    }

    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser); // Retorna 200 OK con el cuerpo del usuario actualizado
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/recuerdos")
    public ResponseEntity<List<MemoryDTO>> getUserMemories(@PathVariable Long id) {
        List<MemoryDTO> memories = userService.getUserMemories(id);
        return ResponseEntity.ok(memories);
    }

    @GetMapping("/{id}/invitaciones")
    public ResponseEntity<List<InvitationDTO>> getUserInvitations(@PathVariable Long id) {
        List<InvitationDTO> invitations = userService.getUserInvitations(id);
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDTO> notifications = userService.getUserNotifications(id);
        return ResponseEntity.ok(notifications);
    }

    //Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


}
