package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDto;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final EventRepository eventRepository;
    private final MemoryRepository memoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // Inyección por constructor para evitar dependencias circulares
    public UserAccountService(UserAccountRepository userAccountRepository,
                              EventRepository eventRepository,
                              MemoryRepository memoryRepository,
                              PasswordEncoder passwordEncoder,
                              ModelMapper modelMapper) {
        this.userAccountRepository = userAccountRepository;
        this.eventRepository = eventRepository;
        this.memoryRepository = memoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    public UserDTO userProfile(String email) {
        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + email);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsernameField());
        return userDTO;
    }

    // Buscar usuarios por nombre de usuario
    public List<UserDTO> searchByUsername(String username) {
        Pageable limit = PageRequest.of(0, 10);  // Página 0 y tamaño de página 10 (máximo 10 resultados)
        return userAccountRepository.findByUsernameContainingIgnoreCase(username, limit)
                .stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setFirstName(user.getFirstName());
                    userDTO.setLastName(user.getLastName());
                    userDTO.setUsername(user.getUsernameField());
                    return userDTO;
                })
                .collect(Collectors.toList());
    }


    // Obtener usuario por ID
    public UserDTO getUserById(Long id) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsernameField()); // Asegura que se utiliza el username correcto

        return userDTO;
    }



    // Actualizar usuario
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userAccountRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setUsername(userDTO.getUsername());
                    // Guarda los cambios y devuelve el objeto actualizado mapeado a UserDTO
                    userAccountRepository.save(user);

                    // Crear un UserDTO utilizando getUsernameField para obtener el valor de username
                    UserDTO updatedUserDTO = new UserDTO();
                    updatedUserDTO.setFirstName(user.getFirstName());
                    updatedUserDTO.setLastName(user.getLastName());
                    updatedUserDTO.setUsername(user.getUsernameField()); // Utilizar getUsernameField para el username

                    return updatedUserDTO;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public boolean isAuthorized(Long userId, String email) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }





    // Eliminar usuario
    public void deleteUser(Long id) {
        eventRepository.deleteByOrganizer_Id(id);
        memoryRepository.deleteByUserAccountId(id);
        userAccountRepository.deleteById(id);
    }

    // Obtener recuerdos del usuario
    public List<MemoryDTO> getUserMemories(Long userId) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getMemories().stream()
                        .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }


    // Obtener notificaciones del usuario
    public List<NotificationDTO> getUserNotifications(Long userId) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getNotifications().stream()
                        .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }




}
