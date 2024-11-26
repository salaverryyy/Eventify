package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationStatus;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDto;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccountService;
import com.eventos.recuerdos.eventify_project.user.dto.UpdateUserDto;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private InvitationRepository invitationRepository;

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
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto, Principal principal) {
        // Verifica si el usuario autenticado coincide con el usuario a actualizar
        if (!userAccountService.isAuthorized(id, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Llamar al servicio para actualizar
        UpdateUserDto updatedUser = userAccountService.updateUser(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }



    //Eliminar usuario
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //obtener los albums donde participa el usuario
    @GetMapping("/{id}/recuerdos")
    public ResponseEntity<List<MemoryDTO>> getUserMemories(@PathVariable Long id) {
        List<MemoryDTO> memories = userAccountService.getUserMemories(id);
        return ResponseEntity.ok(memories);
    }

    //obtener las notificaciones de un usuario en especifico
    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long id) {
        List<NotificationDTO> notifications = userAccountService.getUserNotifications(id);
        return ResponseEntity.ok(notifications);
    }


    @PostMapping("/{id}/add-friend")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String friendUsername = payload.get("friendUsername");
        userAccountService.addFriendByUsername(id, friendUsername);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}/remove-friend/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userAccountService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<UserDTO>> getFriends(@PathVariable Long id) {
        Set<UserDTO> friends = userAccountService.getFriends(id);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{email}/accepted-invitations")
    public ResponseEntity<List<Invitation>> getAcceptedInvitationsByEmail(@PathVariable String email) {
        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        // Obtener las invitaciones aceptadas
        List<Invitation> acceptedInvitations = invitationRepository.findAllByEmailAndStatus(email, InvitationStatus.ACCEPTED);

        if (acceptedInvitations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(acceptedInvitations);
    }
}

