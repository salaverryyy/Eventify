package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final PublicationRepository publicationRepository;

    public AdminService(
            UserRepository userRepository,
            InvitationRepository invitationRepository,
            PublicationRepository publicationRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.publicationRepository = publicationRepository;
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener todas las invitaciones
    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

    // Obtener todas las publicaciones
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    // Otros métodos administrativos adicionales, si es necesario, pueden agregarse aquí
}