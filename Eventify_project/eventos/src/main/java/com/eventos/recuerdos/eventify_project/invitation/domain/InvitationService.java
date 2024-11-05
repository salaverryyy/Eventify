package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDto;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationByLinkDto;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationDto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;

@Service
public class InvitationService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String BASE_URL = "http://localhost:3000/album/";

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

    public InvitationStatusDto getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        return modelMapper.map(invitation, InvitationStatusDto.class);
    }

    public void acceptInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatusDto.ACCEPTED);
    }

    public void rejectInvitation(Long id) {
        updateInvitationStatus(id, InvitationStatusDto.REJECTED);
    }

    private void updateInvitationStatus(Long id, InvitationStatusDto status) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitation.setStatus(status);
        invitationRepository.save(invitation);
    }

    public List<InvitationDto> sendInvitationByQr(InvitationByQrDto dto) throws MessagingException {
        List<InvitationDto> invitations = new ArrayList<>();
        for (String guestEmail : dto.getGuestEmails()) {
            Invitation invitation = createInvitation(dto.getUserId(), dto.getEventId(), guestEmail);
            invitation.setQrCode(generateQRCode(generateAlbumLink(dto.getEventId())));

            sendInvitationEmail(guestEmail, dto.getEventId());
            invitations.add(mapAndSaveInvitation(invitation));
        }
        return invitations;
    }

    public List<InvitationDto> sendInvitationByLink(InvitationByLinkDto dto) throws MessagingException {
        List<InvitationDto> invitations = new ArrayList<>();
        String albumLink = generateAlbumLink(dto.getEventId());

        for (String guestEmail : dto.getGuestEmails()) {
            Invitation invitation = createInvitation(dto.getUserId(), dto.getEventId(), guestEmail);
            invitation.setInvitationLink(albumLink);

            sendInvitationEmailWithLink(guestEmail, albumLink);
            invitations.add(mapAndSaveInvitation(invitation));
        }
        return invitations;
    }

    private String generateAlbumLink(Long albumId) {
        return BASE_URL + albumId;
    }

    private String generateQRCode(String albumLink) {
        String qrApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + albumLink;
        RestTemplate restTemplate = new RestTemplate();
        byte[] qrImageBytes = restTemplate.getForObject(qrApiUrl, byte[].class);
        return Base64.getEncoder().encodeToString(qrImageBytes);
    }

    private void sendInvitationEmail(String email, Long albumId) throws MessagingException {
        String albumLink = generateAlbumLink(albumId);
        String qrCode = generateQRCode(albumLink);

        String content = "<p>Escanea el siguiente QR para acceder al álbum:</p>"
                + "<img src='data:image/png;base64," + qrCode + "' alt='QR Code' />";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Invitación al Álbum Virtual");
        helper.setText(content, true);

        mailSender.send(message);
    }

    private void sendInvitationEmailWithLink(String email, String albumLink) throws MessagingException {
        String content = "<p>Haz clic en el siguiente enlace para acceder al álbum:</p>"
                + "<a href='" + albumLink + "'>Acceder al álbum</a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Invitación al Álbum Virtual");
        helper.setText(content, true);

        mailSender.send(message);
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
        invitation.setStatus(InvitationStatusDto.PENDING);
        return invitation;
    }

    private InvitationDto mapAndSaveInvitation(Invitation invitation) {
        Invitation savedInvitation = invitationRepository.save(invitation);
        InvitationDto resultDto = modelMapper.map(savedInvitation, InvitationDto.class);
        resultDto.setUserId(invitation.getUsuarioInvitador().getId());
        return resultDto;
    }

    public InvitationDto getInvitationByQR(String qrCode) {
        Invitation invitation = invitationRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con código QR: " + qrCode));
        return modelMapper.map(invitation, InvitationDto.class);
    }

    public InvitationDto getInvitationByLink(String token) {
        Invitation invitation = invitationRepository.findByInvitationLinkContaining(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con token: " + token));
        return modelMapper.map(invitation, InvitationDto.class);
    }

    public void deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitationRepository.delete(invitation);
    }

    public List<InvitationDto> getAllInvitations() {
        return invitationRepository.findAll().stream()
                .map(invitation -> modelMapper.map(invitation, InvitationDto.class))
                .collect(Collectors.toList());
    }
}
