package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByLinkDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDTO;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserAccountRepository userAccountRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public InvitationService(InvitationRepository invitationRepository,
                             UserAccountRepository userAccountRepository,
                             EventRepository eventRepository,
                             ModelMapper modelMapper) {
        this.invitationRepository = invitationRepository;
        this.userAccountRepository = userAccountRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    public InvitationStatusDTO getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        return modelMapper.map(invitation, InvitationStatusDTO.class);
    }

    public void acceptInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatus.ACCEPTED);
    }

    public void rejectInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatus.REJECTED);
    }

    private void updateInvitationStatus(Long id, InvitationStatus status) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitation.setStatus(status);
        invitationRepository.save(invitation);
    }


    public InvitationDTO sendInvitationByQr(InvitationByQrDTO dto) {
        Invitation invitation = createInvitation(dto.getUserId(), dto.getEventId(), dto.getGuestEmail());
        invitation.setQrCode(dto.getQrCode());

        if (dto.getInvitedUserId() != null) {
            UserAccount invitedUserAccount = userAccountRepository.findById(dto.getInvitedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario invitado no encontrado"));
            invitation.setUsuarioInvitado(invitedUserAccount);
        }

        return mapAndSaveInvitation(invitation);
    }

    public InvitationDTO sendInvitationByLink(InvitationByLinkDTO dto) {
        Invitation invitation = createInvitation(dto.getUserId(), dto.getEventId(), dto.getGuestEmail());
        invitation.setInvitationLink(dto.getInvitationLink());
        return mapAndSaveInvitation(invitation);
    }

    private Invitation createInvitation(Long userId, Long eventId, String guestEmail) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + eventId));

        Invitation invitation = new Invitation();
        invitation.setUsuarioInvitador(userAccount);
        invitation.setEvent(event);
        invitation.setGuestEmail(guestEmail);
        invitation.setStatus(InvitationStatus.PENDING); // Asigna el valor del enum correctamente
        return invitation;

    }

    private InvitationDTO mapAndSaveInvitation(Invitation invitation) {
        Invitation savedInvitation = invitationRepository.save(invitation);
        InvitationDTO resultDTO = modelMapper.map(savedInvitation, InvitationDTO.class);
        resultDTO.setUserId(invitation.getUsuarioInvitador().getId());
        return resultDTO;
    }

    public InvitationDTO getInvitationByQR(String qrCode) {
        Invitation invitation = invitationRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con código QR: " + qrCode));
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    public InvitationDTO getInvitationByLink(String token) {
        Invitation invitation = invitationRepository.findByInvitationLinkContaining(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con token: " + token));
        return modelMapper.map(invitation, InvitationDTO.class);
    }

    public void deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitationRepository.delete(invitation);
    }

    public List<InvitationDTO> getAllInvitations() {
        return invitationRepository.findAll().stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDTO.class))
                .collect(Collectors.toList());
    }
}

