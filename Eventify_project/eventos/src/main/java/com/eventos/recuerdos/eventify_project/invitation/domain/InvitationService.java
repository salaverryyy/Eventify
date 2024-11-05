package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDto;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDto;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByLinkDto;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationStatus;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.eventos.recuerdos.eventify_project.InvitationEmailEvent;

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

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
        InvitationStatusDto statusDto = new InvitationStatusDto();
        statusDto.setInvitationStatus(InvitationStatus.ACCEPTED);
        updateInvitationStatus(id, statusDto);
    }

    public void rejectInvitation(Long id) {
        InvitationStatusDto statusDto = new InvitationStatusDto();
        statusDto.setInvitationStatus(InvitationStatus.REJECTED);
        updateInvitationStatus(id, statusDto);
    }

    private void updateInvitationStatus(Long id, InvitationStatusDto statusDto) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitation.setStatus(statusDto.getInvitationStatus());
        invitationRepository.save(invitation);
    }

    public List<InvitationDto> sendInvitationByQr(InvitationByQrDto dto) throws MessagingException {
        List<InvitationDto> invitations = new ArrayList<>();
        String albumLink = generateAlbumLink(dto.getEventId());

        for (String guestEmail : dto.getGuestEmails()) {
            Invitation invitation = createInvitation(dto.getUserId(), dto.getEventId(), guestEmail);
            String qrCode = generateQRCode(albumLink);
            invitation.setQrCode(qrCode);

            invitations.add(mapAndSaveInvitation(invitation));

            // Publicar el evento de correo de invitación
            eventPublisher.publishEvent(new InvitationEmailEvent(guestEmail, qrCode, albumLink));
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

    public void sendInvitationEmail(String email, String albumLink, String qrCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Invitación al Álbum Virtual");

        // Crear el contexto para el template
        Context context = new Context();
        context.setVariable("invitationLink", albumLink);

        // Generar contenido HTML usando el template
        String htmlContent = templateEngine.process("templates/invitation-email-template.html", context);
        helper.setText(htmlContent, true);

        // Adjuntar el QR como imagen en el correo
        byte[] qrCodeBytes = Base64.getDecoder().decode(qrCode);
        helper.addInline("qrCode", new ByteArrayResource(qrCodeBytes), "image/png");

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
        invitation.setStatus(InvitationStatus.PENDING);
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
