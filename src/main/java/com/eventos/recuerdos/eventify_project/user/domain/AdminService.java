package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemorySummaryDto;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.dto.UserAccountDto;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ModelMapper modelMapper;

    private final UserAccountRepository userAccountRepository;
    private final InvitationRepository invitationRepository;
    private final PublicationRepository publicationRepository;

    public AdminService(
            UserAccountRepository userAccountRepository,
            InvitationRepository invitationRepository,
            PublicationRepository publicationRepository) {
        this.userAccountRepository = userAccountRepository;
        this.invitationRepository = invitationRepository;
        this.publicationRepository = publicationRepository;
    }

    // Obtener todos los usuarios
    public List<UserAccountDto> getAllUsers() {
        return userAccountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserAccountDto convertToDto(UserAccount userAccount) {
        UserAccountDto userDto = modelMapper.map(userAccount, UserAccountDto.class);

        // Mapear solo los datos esenciales de cada Memory
        List<MemorySummaryDto> memories = userAccount.getMemories().stream()
                .map(memory -> modelMapper.map(memory, MemorySummaryDto.class))
                .collect(Collectors.toList());
        userDto.setMemories(memories);

        return userDto;
    }

    // Obtener todas las invitaciones
    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

    // Obtener todas las publicaciones
    public List<PublicationDTO> getAllPublications() {
        return publicationRepository.findAll().stream()
                .map(publication -> {
                    PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
                    publicationDTO.setUserId(publication.getAuthor() != null ? publication.getAuthor().getId() : null);
                    return publicationDTO;
                })
                .collect(Collectors.toList());
    }


    // Otros métodos administrativos adicionales, si es necesario, pueden agregarse aquí
}