package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import jakarta.mail.MessagingException;
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
    private MemoryRepository memoryRepository;

    // Obtener el estado de una invitación
    @GetMapping("/{id}")
    public ResponseEntity<InvitationStatusDto> getInvitationStatus(@PathVariable Long id) {
        InvitationStatusDto status = invitationService.getInvitationStatus(id);
        return ResponseEntity.ok(status);
    }

    // Aceptar una invitación
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
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
    public ResponseEntity<List<InvitationDto>> sendInvitationByQr(@RequestBody InvitationRequestDto invitationRequestDto, Principal principal) throws MessagingException {
        List<InvitationDto> createdInvitations = invitationService.sendInvitationByQr(invitationRequestDto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitations);
    }


    // Verificar código de acceso de un Memory
    @GetMapping("/verify-access-code/{code}")
    public ResponseEntity<String> verifyAccessCode(@PathVariable String code) {
        Memory memory = memoryRepository.findByAccessCode(code);

        if (memory == null) {
            throw new ResourceNotFoundException("Código de acceso no válido");
        }

        return ResponseEntity.ok("Código de acceso válido para el álbum con ID: " + memory.getId());
    }


    // Eliminar invitación por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvitation(@PathVariable Long id) {
        invitationService.deleteInvitation(id);
        return ResponseEntity.ok("Invitación eliminada con éxito.");
    }

    // Obtener todas las invitaciones creadas
    @GetMapping
    public ResponseEntity<List<InvitationDto>> getAllInvitations() {
        List<InvitationDto> invitations = invitationService.getAllInvitations();
        return ResponseEntity.ok(invitations);
    }
}
