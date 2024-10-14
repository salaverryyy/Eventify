package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final MemoryRepository memoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // Inyección por constructor para evitar dependencias circulares
    public UserService(UserRepository userRepository,
                       EventRepository eventRepository,
                       MemoryRepository memoryRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.memoryRepository = memoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    // Obtener usuario por ID
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }


    // Actualizar usuario
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDTO.getUsername());
                    user.setEmail(userDTO.getEmail());
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    return modelMapper.map(userRepository.save(user), UserDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        eventRepository.deleteByOrganizer_Id(id);
        memoryRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    // Obtener recuerdos del usuario
    public List<MemoryDTO> getUserMemories(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getMemories().stream()
                        .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    // Obtener invitaciones del usuario
    public List<InvitationDTO> getUserInvitations(Long userId) {
        return userRepository.findById(userId)
                .map(user -> Stream.concat(
                                        user.getInvitationsSent().stream(),
                                        user.getInvitationsReceived().stream()
                                )
                                .map(invitation -> {
                                    InvitationDTO dto = modelMapper.map(invitation, InvitationDTO.class);
                                    dto.setUserId(user.getId());
                                    return dto;
                                })
                                .collect(Collectors.toList())
                )
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }


    // Obtener notificaciones del usuario
    public List<NotificationDTO> getUserNotifications(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getNotifications().stream()
                        .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    // Buscar usuarios por nombre de usuario
    public List<User> searchByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

}
