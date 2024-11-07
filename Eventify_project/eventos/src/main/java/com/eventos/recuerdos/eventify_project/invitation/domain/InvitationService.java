package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.InvitationEmailEvent;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Base64;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    private static final String CONFIRMATION_URL = "http://localhost:3000/confirm/";

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public InvitationStatusDto getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con ID: " + id));
        InvitationStatusDto statusDto = new InvitationStatusDto();
        statusDto.setInvitationStatus(invitation.getStatus());
        return statusDto;
    }

    public void acceptInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatus.ACCEPTED);
    }

    public void rejectInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatus.REJECTED);
    }

    private void updateInvitationStatus(Long id, InvitationStatus status) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con ID: " + id));
        invitation.setStatus(status);
        invitationRepository.save(invitation);
    }

    public List<InvitationDto> sendInvitationByQr(InvitationRequestDto invitationRequestDto, Principal principal) {
        String senderEmail = principal.getName();
        UserAccount sender = userAccountRepository.findByEmail(senderEmail);

        if (sender == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con correo: " + senderEmail);
        }

        Memory memory = memoryRepository.findByAccessCode(invitationRequestDto.getAccessCode());
        if (memory == null) {
            throw new ResourceNotFoundException("Memory no encontrado con código de acceso: " + invitationRequestDto.getAccessCode());
        }

        List<InvitationDto> invitations = new ArrayList<>();
        for (String username : invitationRequestDto.getUsernames()) {
            UserAccount invitedUser = userAccountRepository.findByUsername(username);
            if (invitedUser == null) {
                throw new ResourceNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
            }

            String confirmationLink = generateConfirmationLink();
            InvitationDto invitationDto = createAndSaveInvitation(invitedUser.getEmail(), sender, memory, confirmationLink);
            invitations.add(invitationDto);

            InvitationEmailEvent emailEvent = new InvitationEmailEvent(
                    invitedUser.getEmail(),
                    invitationDto.getQrCode(),
                    confirmationLink
            );
            eventPublisher.publishEvent(emailEvent);
        }
        return invitations;
    }

    private InvitationDto createAndSaveInvitation(String guestEmail, UserAccount usuarioInvitador, Memory memory, String confirmationLink) {
        Invitation invitation = new Invitation();
        invitation.setGuestEmail(guestEmail);
        invitation.setUsuarioInvitador(usuarioInvitador);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setAlbumLink(memory.getAlbumLink());
        invitation.setMemory(memory);

        Invitation savedInvitation = invitationRepository.save(invitation);
        InvitationDto invitationDto = modelMapper.map(savedInvitation, InvitationDto.class);

        String qrCode = generateQRCode(confirmationLink);
        invitationDto.setQrCode(qrCode);
        invitationDto.setAlbumLink(memory.getAlbumLink());
        invitationDto.setConfirmationLink(confirmationLink);

        return invitationDto;
    }

    private String generateConfirmationLink() {
        String uniqueToken = UUID.randomUUID().toString();
        return CONFIRMATION_URL + uniqueToken;
    }

    private String generateQRCode(String confirmationLink) {
        String qrApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + confirmationLink;
        RestTemplate restTemplate = new RestTemplate();
        byte[] qrImageBytes = restTemplate.getForObject(qrApiUrl, byte[].class);
        return Base64.getEncoder().encodeToString(qrImageBytes);
    }

    public void deleteInvitation(Long id) {
        if (!invitationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invitación no encontrada con ID: " + id);
        }
        invitationRepository.deleteById(id);
    }

    public List<InvitationDto> getAcceptedInvitations(Long memoryId) {
        List<Invitation> acceptedInvitations = invitationRepository.findAllByMemoryIdAndStatus(memoryId, InvitationStatus.ACCEPTED);
        return acceptedInvitations.stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDto.class))
                .collect(Collectors.toList());
    }

    public List<InvitationDto> getAllInvitations() {
        return invitationRepository.findAll().stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDto.class))
                .collect(Collectors.toList());
    }

    public MemoryDTO verifyAccessCode(String accessCode) {
        Memory memory = memoryRepository.findByAccessCode(accessCode);
        if (memory == null) {
            throw new ResourceNotFoundException("El código de acceso proporcionado no es válido.");
        }
        return modelMapper.map(memory, MemoryDTO.class);
    }
}
