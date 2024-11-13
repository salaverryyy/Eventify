package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

    // Obtener el estado de una invitación
    @GetMapping("/{id}")
    public ResponseEntity<InvitationStatusDto> getInvitationStatus(@PathVariable Long id) {
        InvitationStatusDto status = invitationService.getInvitationStatus(id);
        return ResponseEntity.ok(status);
    }

    // Aceptar una invitación
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        invitationService.acceptInvitation(id, userEmail);
        return ResponseEntity.ok("Invitación aceptada.");
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
    @GetMapping("/verify-access-code/{code}")
    public ResponseEntity<String> verifyAccessCode(@PathVariable String code) {
        MemoryDTO memory = invitationService.verifyAccessCode(code);
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
