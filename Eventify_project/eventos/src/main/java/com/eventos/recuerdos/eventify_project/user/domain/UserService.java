package com.eventos.recuerdos.eventify_project.user.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.invitation.dto.InvitationDTO;
import com.eventos.recuerdos.eventify_project.memory.dto.MemoryDTO;
import com.eventos.recuerdos.eventify_project.notification.dto.NotificationDTO;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    //metodos
    //obtener usuario por Id
    public UserDTO getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            return modelMapper.map(userOptional.get(),UserDTO.class);

        }throw new RuntimeException("User not found");
    }

    //crear nuevo usuario
    public UserDTO createUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
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
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
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
                .map(invitation -> modelMapper.map(invitation, InvitationDTO.class))
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

}
