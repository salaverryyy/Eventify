package com.eventos.recuerdos.eventify_project.invitation.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByLinkDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationByQrDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationStatusDTO;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
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
        // Obtener el usuario por su ID
        User user = userRepository.findById(invitationByQrDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + invitationByQrDTO.getUserId()));

        // Crear la entidad Invitation
        Invitation invitation = new Invitation();
        invitation.setUsuarioInvitador(user); // Asociar la invitación con el usuario
        invitation.setQrCode(invitationByQrDTO.getQrCode());
        invitation.setGuestEmail(invitationByQrDTO.getGuestEmail());
        invitation.setStatus("PENDING");

        // Guardar la invitación en la base de datos
        invitation = invitationRepository.save(invitation);

        // Imprimir el userId para verificar
        System.out.println("User ID al guardar: " + user.getId());

        // Crear y devolver el DTO con el userId incluido
        InvitationDTO resultDTO = modelMapper.map(invitation, InvitationDTO.class);
        resultDTO.setUserId(user.getId());  // Asignar el userId manualmente en el DTO

        return resultDTO;
    }



    // Enviar una invitación por enlace (token)
    public InvitationDTO sendInvitationByLink(InvitationByLinkDTO InvitationByLinkDTO) {
        // Obtener el usuario por su ID
        User user = userRepository.findById(InvitationByLinkDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + InvitationByLinkDTO.getUserId()));

        // Crear una nueva invitación
        Invitation invitation = new Invitation();
        invitation.setGuestEmail(InvitationByLinkDTO.getGuestEmail());
        invitation.setStatus("PENDING");
        invitation.setUsuarioInvitador(user);

        // En lugar de guardar la URL completa, solo guardamos el token
        String token = InvitationByLinkDTO.getInvitationLink(); // Esto sería el token, por ejemplo "abc123"
        invitation.setInvitationLink(token);

        // Guardar la invitación
        invitation = invitationRepository.save(invitation);

        // Mapear a DTO y devolver la invitación creada
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

