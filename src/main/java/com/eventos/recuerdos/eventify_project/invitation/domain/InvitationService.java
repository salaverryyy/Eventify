package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.InvitationEmailEvent;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.*;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.securityconfig.domain.JwtService;
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

    private static final String CONFIRMATION_URL = "http://localhost:5173/confirm/";

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtService jwtService;

    public InvitationStatusDto getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con ID: " + id));
        InvitationStatusDto statusDto = new InvitationStatusDto();
        statusDto.setInvitationStatus(invitation.getStatus());
        return statusDto;
    }

    public String acceptInvitation(String invitationUUID, String userEmail) {
        Invitation invitation = invitationRepository.findByUuid(invitationUUID)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        UserAccount invitedUser = userAccountRepository.findByEmail(userEmail);
        if (invitedUser == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        // Actualizar el estado de la invitación a ACCEPTED si no lo está ya
        if (invitation.getStatus() != InvitationStatus.ACCEPTED) {
            invitation.setStatus(InvitationStatus.ACCEPTED);
            invitationRepository.save(invitation);

            // Agregar el usuario a la lista de participantes del Memory
            Memory memory = invitation.getMemory();
            if (!memory.getParticipants().contains(invitedUser)) {
                memory.getParticipants().add(invitedUser);
                memoryRepository.save(memory);
            }

            // Agregar la invitación a la lista de invitaciones aceptadas del usuario
            if (!invitedUser.getAcceptedInvitations().contains(invitation)) {
                invitedUser.getAcceptedInvitations().add(invitation);
                userAccountRepository.save(invitedUser);
            }
        }

        // Devolver el albumLink asociado al Memory de la invitación
        return invitation.getMemory().getAlbumLink();
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

            // Crear y guardar la invitación antes de generar el enlace de confirmación
            Invitation invitation = new Invitation();
            invitation.setGuestEmail(invitedUser.getEmail());
            invitation.setUsuarioInvitador(sender);
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setMemory(memory);
            invitation.setAlbumLink(memory.getAlbumLink());

            invitationRepository.save(invitation);

            // Generar el enlace de confirmación usando el UUID de la invitación guardada
            String confirmationLink = generateConfirmationLink(invitation);

            // Crear el DTO de la invitación y agregarlo a la lista
            InvitationDto invitationDto = createAndSaveInvitation(invitedUser.getEmail(), sender, memory, confirmationLink);
            invitations.add(invitationDto);

            // Crear y publicar el evento de correo electrónico
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

    private String generateConfirmationLink(Invitation invitation) {
        return CONFIRMATION_URL + invitation.getUuid();
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

    public MemoryDTO verifyAccessCode(String accessCode, Long userId) {
        // Buscar el usuario por userId
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Buscar el Memory asociado al código de acceso
        Memory memory = memoryRepository.findByAccessCode(accessCode);
        if (memory == null) {
            throw new ResourceNotFoundException("El código de acceso proporcionado no es válido.");
        }

        // Agregar al usuario como participante si no está ya
        if (!memory.getParticipants().contains(user)) {
            memory.getParticipants().add(user);
            memoryRepository.save(memory);
        }

        // Crear una invitación ficticia si el usuario no tiene una invitación aceptada para este Memory
        boolean hasAcceptedInvitation = user.getAcceptedInvitations().stream()
                .anyMatch(invitation -> invitation.getMemory().equals(memory));
        if (!hasAcceptedInvitation) {
            Invitation newInvitation = new Invitation();
            newInvitation.setUuid(UUID.randomUUID().toString()); // Generar un UUID único
            newInvitation.setMemory(memory);
            newInvitation.setStatus(InvitationStatus.ACCEPTED); // Establecer el estado como ACCEPTED
            newInvitation.setUsuarioInvitado(user); // Configurar el usuario invitado
            newInvitation.setUsuarioInvitador(memory.getUserAccount()); // Establecer al creador del Memory como usuario invitador
            invitationRepository.save(newInvitation);

            user.getAcceptedInvitations().add(newInvitation); // Agregar la invitación al usuario
            userAccountRepository.save(user); // Guardar cambios en el usuario
        }

        // Devolver el DTO del Memory
        return modelMapper.map(memory, MemoryDTO.class);
    }




}
