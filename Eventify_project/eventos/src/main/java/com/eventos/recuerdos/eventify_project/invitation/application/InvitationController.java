package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationByLinkDto;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDto;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationDto;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

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
    public ResponseEntity<List<InvitationDto>> sendInvitationByQr(@RequestBody InvitationByQrDto invitationByQrDto) throws MessagingException {
        List<InvitationDto> createdInvitations = invitationService.sendInvitationByQr(invitationByQrDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitations);
    }

    // Enviar invitación por Link
    @PostMapping("/sendByLink")
    public ResponseEntity<List<InvitationDto>> sendInvitationByLink(@RequestBody InvitationByLinkDto invitationByLinkDto) throws MessagingException {
        List<InvitationDto> invitations = invitationService.sendInvitationByLink(invitationByLinkDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(invitations);
    }

    // Acceder a una invitación mediante un enlace (token)
    @GetMapping("/link/{token}")
    public ResponseEntity<InvitationDto> getInvitationByLink(@PathVariable String token) {
        InvitationDto invitation = invitationService.getInvitationByLink(token);
        return ResponseEntity.ok(invitation);
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
