package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.invitation.infrastructure.InvitationRepository;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ModelMapper modelMapper;

    //metodos
    //obtener usuario por Id
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    //crear nuevo usuario
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setUserCreationDate(LocalDate.now()); // Establece la fecha de creación
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }


    //actualizar usuario
    public UserDTO updateUser(Long id, UserDTO userDTO){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        }
        throw new RuntimeException("User not found");
    }

    //eliminar usuario
    public void deleteUser(Long id) {
        // Eliminar todas las publicaciones asociadas al usuario
        List<Publication> publications = publicationRepository.findByUserId(id);
        publicationRepository.deleteAll(publications);

        // Luego eliminar al usuario
        userRepository.deleteById(id);
    }


    //Obtener todos los recuerdos creados por el usuario
    public List<MemoryDTO> getUserMemories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return user.getMemories().stream()
                .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                .collect(Collectors.toList());
    }

    //Obtener todas las invitaciones (enviadas y recibidas) por el usuario.
    public List<InvitationDTO> getUserInvitations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return user.getInvitations().stream()
                .map(invitation -> {
                    // Mapeo de la entidad a DTO
                    InvitationDTO invitationDTO = modelMapper.map(invitation, InvitationDTO.class);
                    // Asignación manual del userId
                    invitationDTO.setUserId(user.getId());
                    return invitationDTO;
                })
                .collect(Collectors.toList());
    }



    //Obtener todas las notificaciones de un usuario.
    public List<NotificationDTO> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return user.getNotifications().stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    //Obtener todos los Usuarios
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

}
