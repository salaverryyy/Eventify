package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.InvitationEmailEvent;
import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String BASE_URL = "http://localhost:3000/album/"; // Ajusta para producción si es necesario

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;


    public InvitationStatusDto getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con ID: " + id));
        return modelMapper.map(invitation.getStatus(), InvitationStatusDto.class);
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

        // Recuperar el Memory asociado usando el accessCode
        Memory memory = memoryRepository.findByAccessCode(invitationRequestDto.getAccessCode());
        if (memory == null) {
            throw new ResourceNotFoundException("Memory no encontrado con código de acceso: " + invitationRequestDto.getAccessCode());
        }

        String albumLink = memory.getAlbumLink();  // Obtenemos el enlace del álbum ya asignado en el Memory

        List<InvitationDto> invitations = new ArrayList<>();

        for (String username : invitationRequestDto.getUsernames()) {
            UserAccount invitedUser = userAccountRepository.findByUsername(username);
            if (invitedUser == null) {
                throw new ResourceNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
            }

            // Crear y guardar la invitación
            InvitationDto invitationDto = createAndSaveInvitation(invitedUser.getEmail(), sender, albumLink);
            invitations.add(invitationDto);

            // Emitir el evento para enviar el correo
            InvitationEmailEvent emailEvent = new InvitationEmailEvent(
                    invitedUser.getEmail(),
                    invitationDto.getQrCode(),
                    albumLink
            );
            eventPublisher.publishEvent(emailEvent);
        }

        return invitations;
    }

    private InvitationDto createAndSaveInvitation(String guestEmail, UserAccount usuarioInvitador, String albumLink) {
        Invitation invitation = new Invitation();
        invitation.setGuestEmail(guestEmail);
        invitation.setUsuarioInvitador(usuarioInvitador);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setAlbumLink(albumLink);  // Asegurarse de que el enlace del álbum esté asignado aquí

        Invitation savedInvitation = invitationRepository.save(invitation);
        InvitationDto invitationDto = modelMapper.map(savedInvitation, InvitationDto.class);

        // Generar el código QR en base64 para el enlace del álbum
        String qrCode = generateQRCode(albumLink);
        invitationDto.setQrCode(qrCode);
        invitationDto.setAlbumLink(albumLink);  // Asegurar que el albumLink esté en el DTO de respuesta

        return invitationDto;
    }




    private String generateAlbumLink() {
        String uniqueToken = UUID.randomUUID().toString();
        return BASE_URL + uniqueToken;
    }

    // Genera el código QR en base64 a partir del enlace del álbum
    private String generateQRCode(String albumLink) {
        String qrApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + albumLink;
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
