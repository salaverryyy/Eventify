package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccountService;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> userProfile(Principal principal) {
        UserDTO userDTO = userAccountService.userProfile(principal.getName());
        return ResponseEntity.ok(userDTO);
    }



    //busqueda de usuarios
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String username) {
        List<UserDTO> userDTOs = userAccountService.searchByUsername(username);
        return ResponseEntity.ok(userDTOs);
    }




    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userAccountService.getUserById(id);
        return ResponseEntity.ok(userDTO); // Retorna 200 OK con el cuerpo del usuario
    }


    // Actualizar un usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO, Principal principal) {
        // Verifica si el usuario autenticado coincide con el usuario a actualizar
        if (!userAccountService.isAuthorized(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO updatedUser = userAccountService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    //Eliminar usuario
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?>deleteUser(@PathVariable Long id) {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    //obtener los albums donde participa el usuario
    @GetMapping("/{id}/recuerdos")
    public ResponseEntity<List<MemoryDTO>> getUserMemories(@PathVariable Long id) {
        List<MemoryDTO> memories = userAccountService.getUserMemories(id);
        return ResponseEntity.ok(memories);
    }

    //obtener las invitaciones de un usuario en especifico
    @GetMapping("/{id}/invitaciones")
    public ResponseEntity<List<InvitationDTO>> getUserInvitations(@PathVariable Long id) {
        List<InvitationDTO> invitations = userAccountService.getUserInvitations(id);
        return ResponseEntity.ok(invitations);
    }

    //obtener las notificaciones de un usuario en especifico
    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDTO> notifications = userAccountService.getUserNotifications(id);
        return ResponseEntity.ok(notifications);
    }

}
