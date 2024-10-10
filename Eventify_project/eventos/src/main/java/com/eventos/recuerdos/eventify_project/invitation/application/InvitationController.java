package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByLinkDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDTO;
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


    //Obtener el estado de una invitación
    @GetMapping("/{id}")
    public ResponseEntity<InvitationStatusDTO> getInvitationStatus(@PathVariable Long id) {
        InvitationStatusDTO status = invitationService.getInvitationStatus(id);
        return ResponseEntity.ok(status);
    }



    //Aceptar una invitación
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
        return ResponseEntity.ok("Invitación aceptada.");
    }

    //Rechazar una invitación
    @PutMapping("/{id}/decline")
    public ResponseEntity<String> rejectInvitation(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.ok("Invitación rechazada.");
    }

    // Enviar una invitación por QR
    @PostMapping("/sendByQr")
    public ResponseEntity<InvitationDTO> sendInvitationByQr(@RequestBody InvitationByQrDTO invitationByQrDTO) {
        InvitationDTO createdInvitation = invitationService.sendInvitationByQr(invitationByQrDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
    }

    // Enviar una invitación por enlace
    @PostMapping("/sendByLink")
    public ResponseEntity<InvitationDTO> sendInvitationByLink(@RequestBody InvitationByLinkDTO invitationByLinkDTO) {
        InvitationDTO createdInvitation = invitationService.sendInvitationByLink(invitationByLinkDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
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



    //eliminar invitacion por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvitation(@PathVariable Long id) {
        invitationService.deleteInvitation(id);
        return ResponseEntity.ok("Invitación eliminada con éxito.");
    }


    //obtener todas las invitaciones creadas
    @GetMapping
    public ResponseEntity<List<InvitationDTO>> getAllInvitations() {
        List<InvitationDTO> invitations = invitationService.getAllInvitations();
        return ResponseEntity.ok(invitations);
    }


}
