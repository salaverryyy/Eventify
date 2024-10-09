package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;


    //Obtener el estado de una invitación
    @GetMapping("/{id}")
    public ResponseEntity<InvitationDTO> getInvitationStatus(@PathVariable Long id) {
        InvitationDTO invitation = invitationService.getInvitationStatus(id);
        return ResponseEntity.ok(invitation);
    }

    //Enviar una invitación a un usuario
    @PostMapping
    public ResponseEntity<InvitationDTO> sendInvitation(@RequestBody InvitationDTO invitationDTO) {
        InvitationDTO createdInvitation = invitationService.sendInvitation(invitationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
    }

    //Aceptar una invitación
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
        return ResponseEntity.ok("Invitación aceptada.");
    }

    //Rechazar una invitación
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<String> rejectInvitation(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.ok("Invitación rechazada.");
    }

    //Acceder a una invitación mediante el escaneo de un código QR
    @GetMapping("/qr/{codigoQR}")
    public ResponseEntity<InvitationDTO> getInvitationByQR(@PathVariable String codigoQR) {
        InvitationDTO invitation = invitationService.getInvitationByQR(codigoQR);
        return ResponseEntity.ok(invitation);
    }

    //Acceder a una invitación mediante un enlace (token)
    @GetMapping("/link/{token}")
    public ResponseEntity<InvitationDTO> getInvitationByLink(@PathVariable String token) {
        InvitationDTO invitation = invitationService.getInvitationByLink(token);
        return ResponseEntity.ok(invitation);
    }



}
