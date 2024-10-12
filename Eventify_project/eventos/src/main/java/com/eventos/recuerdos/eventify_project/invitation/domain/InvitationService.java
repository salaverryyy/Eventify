package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByLinkDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDTO;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Obtener el estado de una invitación
    public InvitationStatusDTO getInvitationStatus(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));

        // Retorna solo el estado y el id de la invitación
        InvitationStatusDTO statusDTO = new InvitationStatusDTO();
        statusDTO.setId(invitation.getId());
        statusDTO.setStatus(invitation.getStatus());

        return statusDTO;
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

    public InvitationDTO sendInvitationByQr(InvitationByQrDTO invitationByQrDTO) {
        // Obtener el usuario que envía la invitación
        User userInvitador = userRepository.findById(invitationByQrDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + invitationByQrDTO.getUserId()));

        // Obtener el evento por su ID
        Event event = eventRepository.findById(invitationByQrDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + invitationByQrDTO.getEventId()));

        // Crear la invitación
        Invitation invitation = new Invitation();
        invitation.setUsuarioInvitador(userInvitador);
        invitation.setQrCode(invitationByQrDTO.getQrCode());
        invitation.setGuestEmail(invitationByQrDTO.getGuestEmail());
        invitation.setStatus("PENDING");
        invitation.setEvent(event);

        // Verificar si el usuario invitado ya está registrado
        if (invitationByQrDTO.getInvitedUserId() != null) {
            User invitedUser = userRepository.findById(invitationByQrDTO.getInvitedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario invitado no encontrado con id: " + invitationByQrDTO.getInvitedUserId()));
            invitation.setUsuarioInvitado(invitedUser);  // Asociar el usuario invitado
        }

        // Guardar la invitación
        invitation = invitationRepository.save(invitation);

        // Mapear a DTO y retornar
        InvitationDTO resultDTO = modelMapper.map(invitation, InvitationDTO.class);
        resultDTO.setUserId(userInvitador.getId());
        return resultDTO;
    }




    // Enviar una invitación por enlace (token)
    public InvitationDTO sendInvitationByLink(InvitationByLinkDTO invitationByLinkDTO) {
        // Obtener el usuario por su ID
        User user = userRepository.findById(invitationByLinkDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + invitationByLinkDTO.getUserId()));

        // Obtener el evento por su ID
        Event event = eventRepository.findById(invitationByLinkDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id: " + invitationByLinkDTO.getEventId()));

        // Crear la invitación
        Invitation invitation = new Invitation();
        invitation.setUsuarioInvitador(user);
        invitation.setInvitationLink(invitationByLinkDTO.getInvitationLink());
        invitation.setGuestEmail(invitationByLinkDTO.getGuestEmail());
        invitation.setStatus("PENDING");
        invitation.setEvent(event); // Asociar la invitación con el evento

        // Guardar la invitación
        invitation = invitationRepository.save(invitation);

        // Mapear a DTO
        InvitationDTO resultDTO = modelMapper.map(invitation, InvitationDTO.class);
        resultDTO.setUserId(user.getId());
        return resultDTO;
    }




    // Obtener invitación por QR
    public InvitationDTO getInvitationByQR(String qrCode) {
        Invitation invitation = invitationRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con código QR: " + qrCode));

        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);

        // Verificar si el usuario invitador no es null, y asignar su ID
        if (invitation.getUsuarioInvitador() != null) {
            invitationDTO.setUserId(invitation.getUsuarioInvitador().getId());
        }

        return invitationDTO;
    }



    // Obtener invitación por link
    public InvitationDTO getInvitationByLink(String token) {
        Invitation invitation = invitationRepository.findByInvitationLinkContaining(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con token: " + token));

        // Mapear a DTO
        InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);

        // Asignar manualmente el userId
        if (invitation.getUsuarioInvitador() != null) {
            invitationDTO.setUserId(invitation.getUsuarioInvitador().getId());
        }

        return invitationDTO;
    }



    //metodo para eliminar invitacion con Id
    public void deleteInvitation(Long id) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada con id: " + id));
        invitationRepository.delete(invitation);
    }


    // Obtener todas las invitaciones creadas
    public List<InvitationDTO> getAllInvitations() {
        List<Invitation> invitations = invitationRepository.findAll();

        return invitations.stream()
                .map(invitation -> {
                    InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);
                    // Asignar el userId desde la entidad Invitation al DTO
                    if (invitation.getUsuarioInvitador() != null) {
                        invitationDTO.setUserId(invitation.getUsuarioInvitador().getId());
                    }
                    return invitationDTO;
                })
                .collect(Collectors.toList());
    }

}

