package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.event.infrastructure.EventRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UpdateUserDto;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final EventRepository eventRepository;
    private final MemoryRepository memoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
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
        return modelMapper.map(user, UserDTO.class);
    }

    // Check authorization by matching email
    public boolean isAuthorized(Long userId, String email) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }

    // Search for users by username with a limit
    public List<UserDTO> searchByUsername(String username) {
        Pageable limit = PageRequest.of(0, 10);

        // Mapea explícitamente el username con getUsernameField
        return userAccountRepository.findByUsernameContainingIgnoreCase(username, limit)
                .stream()
                .map(user -> {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    userDTO.setUsername(user.getUsernameField()); // Asegura que se use el username real
                    return userDTO;
                })
                .collect(Collectors.toList());
    }




    // Retrieve user by ID
    public UserDTO getUserById(Long id) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    // Update user with unique checks for email and username
    public UpdateUserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        return userAccountRepository.findById(id)
                .map(user -> {
                    // Validar cambios en el nombre de usuario
                    if (!user.getUsernameField().equals(updateUserDto.getUsername()) &&
                            userAccountRepository.existsByUsername(updateUserDto.getUsername())) {
                        throw new ResourceConflictException("El nombre de usuario ya está en uso.");
                    }

                    // Actualizar los campos relevantes
                    user.setFirstName(updateUserDto.getFirstName());
                    user.setLastName(updateUserDto.getLastName());
                    user.setUsername(updateUserDto.getUsername());

                    // Guardar cambios en la entidad
                    userAccountRepository.save(user);

                    // Mapeo explícito al DTO para evitar el método getUsername()
                    UpdateUserDto result = new UpdateUserDto();
                    result.setUsername(user.getUsernameField()); // Usa getUsernameField()
                    result.setFirstName(user.getFirstName());
                    result.setLastName(user.getLastName());

                    return result;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }


    // Delete user along with associated events and memories
    @Transactional
    public void deleteUser(Long id) {
        eventRepository.deleteByOrganizer_Id(id);
        memoryRepository.deleteByUserAccountId(id);
        userAccountRepository.deleteById(id);
    }

    // Retrieve memories of a user
    public List<MemoryDTO> getUserMemories(Long userId) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getMemories().stream()
                        .map(memory -> modelMapper.map(memory, MemoryDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    // Retrieve notifications of a user
    public List<NotificationDTO> getUserNotifications(Long userId) {
        return userAccountRepository.findById(userId)
                .map(user -> user.getNotifications().stream()
                        .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    // Add a friend by user ID
    public void addFriend(Long userId, Long friendId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAccount friend = userAccountRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userAccountRepository.save(user);
        userAccountRepository.save(friend);
    }

    // Remove a friend by user ID
    public void removeFriend(Long userId, Long friendId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAccount friend = userAccountRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userAccountRepository.save(user);
        userAccountRepository.save(friend);
    }

    // Get friends list of a user
    public Set<UserDTO> getFriends(Long userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends().stream()
                .map(friend -> modelMapper.map(friend, UserDTO.class))
                .collect(Collectors.toSet());
    }

    // Add a friend by username
    public void addFriendByUsername(Long userId, String friendUsername) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserAccount friend = userAccountRepository.findOptionalByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriends().add(friend);
        userAccountRepository.save(user);
    }

    // Save a new user after encoding their password
    public void save(UserAccount user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountRepository.save(user);
    }

    // Find user by email
    public UserAccount findByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    public void saveOther(UserAccount user) {
        userAccountRepository.save(user);
    }
}
