package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.securityconfig.domain.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private JwtService jwtService;

    // Obtener el estado de una invitación
    @GetMapping("/{id}")
    public ResponseEntity<InvitationStatusDto> getInvitationStatus(@PathVariable Long id) {
        InvitationStatusDto status = invitationService.getInvitationStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/confirm/{uuid}")
    public ResponseEntity<Map<String, String>> acceptInvitation(@PathVariable String uuid, Principal principal) {
        String userEmail = principal.getName();
        String albumLink = invitationService.acceptInvitation(uuid, userEmail);

        Map<String, String> response = new HashMap<>();
        response.put("albumLink", albumLink);

        return ResponseEntity.ok(response);
    }




    // Rechazar una invitación
    @PutMapping("/{id}/decline")
    public ResponseEntity<String> rejectInvitation(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.ok("Invitación rechazada.");
    }

    // Enviar invitación por QR
    @PostMapping("/sendByQr")
    public ResponseEntity<List<InvitationDto>> sendInvitationByQr(@RequestBody InvitationRequestDto invitationRequestDto, Principal principal) {
        List<InvitationDto> createdInvitations = invitationService.sendInvitationByQr(invitationRequestDto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitations);
    }

    // Verificar código de acceso y obtener el enlace del álbum
    @GetMapping("/verify-access-code/{accessCode}/{userId}")
    public ResponseEntity<String> verifyAccessCode(
            @PathVariable String accessCode,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        // Extraer el userId del token JWT
        Long extractedUserId = jwtService.extractUserIdFromToken(token);

        // Verificar que el userId del token coincide con el userId pasado en la URL
        if (!userId.equals(extractedUserId)) {
            throw new SecurityException("El userId no coincide con el token de autenticación.");
        }

        // Verificar y agregar al usuario al Memory
        MemoryDTO memory = invitationService.verifyAccessCode(accessCode, userId);
        return ResponseEntity.ok("Código de acceso válido para el álbum en: " + memory.getAlbumLink());
    }




    // Eliminar invitación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvitation(@PathVariable Long id) {
        invitationService.deleteInvitation(id);
        return ResponseEntity.ok("Invitación eliminada con éxito.");
    }

    // Obtener todas las invitaciones aceptadas por memoryId
    @GetMapping("/memory/{memoryId}/accepted")
    public ResponseEntity<List<InvitationDto>> getAcceptedInvitations(@PathVariable Long memoryId) {
        List<InvitationDto> acceptedInvitations = invitationService.getAcceptedInvitations(memoryId);
        return ResponseEntity.ok(acceptedInvitations);
    }

    @GetMapping("/{invitationId}/album-uuid")
    public ResponseEntity<String> getAlbumUUIDByInvitationId(@PathVariable Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        String albumUUID = invitation.getMemory().getAlbumLink();
        return ResponseEntity.ok(albumUUID);
    }


    // Obtener todas las invitaciones creadas
    @GetMapping
    public ResponseEntity<List<InvitationDto>> getAllInvitations() {
        List<InvitationDto> invitations = invitationService.getAllInvitations();
        return ResponseEntity.ok(invitations);
    }
}
