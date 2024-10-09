package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ModelMapper modelMapper
            ;
    // Obtener el estado de una invitación
    public InvitationDTO getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    // Enviar una invitación
    public InvitationDTO sendInvitation(InvitationDTO invitationDTO) {
        Invitation invitation = modelMapper.map(invitationDTO, Invitation.class);
        Invitation savedInvitation = invitationRepository.save(invitation);
        return modelMapper.map(savedInvitation, InvitationDTO.class);
    }

    // Aceptar una invitación
    public void acceptInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitation.setStatus("Aceptada");
        invitationRepository.save(invitation);
    }

    // Rechazar una invitación
    public void rejectInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitation.setStatus("Rechazada");
        invitationRepository.save(invitation);
    }

    // Obtener invitación por QR
    public InvitationDTO getInvitationByQR(String codigoQR) {
        Invitation invitation = invitationRepository.findByQrCode(codigoQR)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con código QR: " + codigoQR));
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    // Obtener invitación por link
    public InvitationDTO getInvitationByLink(String token) {
        Invitation invitation = invitationRepository.findByInvitationLink(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con token: " + token));
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    //obtener todos las invitaciones creadas
    public List<InvitationDTO> getAllInvitations() {
        List<Invitation> invitations = invitationRepository.findAll();
        return invitations.stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDTO.class))
                .collect(Collectors.toList());
    }
}

