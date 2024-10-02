package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
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



}
