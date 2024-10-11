package com.eventos.recuerdos.eventify_project.like.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.like.infrastructure.LikeRepository;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    // Dar "me gusta" a una publicación
    public void likePublication(Long publicationId) {
        // Buscar la publicación por su ID
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        // Obtener el usuario autenticado desde el SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuario no autenticado");
        }

        // Obtener los detalles del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Buscar al usuario en la base de datos usando el username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        // Crear una instancia de PublicationLike y guardarla
        PublicationLike publicationLike = new PublicationLike();
        publicationLike.setPublication(publication);
        publicationLike.setUser(user);

        // Guardar el like en la base de datos
        likeRepository.save(publicationLike);
    }



    // Obtener la lista de usuarios que han dado "me gusta" a una publicación
    public List<UserDTO> getUsersWhoLikedPublication(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        return likeRepository.findByPublication(publication)
                .stream()
                .map(publicationLike -> modelMapper.map(publicationLike.getUser(), UserDTO.class))
                .collect(Collectors.toList());
    }

    // Quitar "me gusta" de una publicación
    public void unlikePublication(Long publicationId, Long userId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        PublicationLike publicationLike = likeRepository.findByPublicationAndUser(publication, user)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no ha dado 'me gusta' a esta publicación."));

        // Quitar el like y reducir el contador
        likeRepository.delete(publicationLike);
        publication.setLikeCount(publication.getLikeCount() - 1);
        publicationRepository.save(publication);  // Actualizar la publicación
    }


    //obtener todos los likes dados
    public List<LikeDTO> getAllLikes() {
        List<PublicationLike> publicationLikes = likeRepository.findAll();
        return publicationLikes.stream()
                .map(publicationLike -> modelMapper.map(publicationLike, LikeDTO.class))
                .collect(Collectors.toList());
    }
}
