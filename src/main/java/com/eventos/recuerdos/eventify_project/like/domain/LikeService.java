package com.eventos.recuerdos.eventify_project.like.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.like.infrastructure.LikeRepository;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.dto.UserDTO;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ModelMapper modelMapper;

    // Dar "me gusta" a una publicación
    public void likePublication(Long publicationId, String userEmail) {
        // Buscar la publicación por su ID
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        // Obtener el usuario autenticado
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);
        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        // Verificar si el usuario ya ha dado "me gusta" a la publicación
        Optional<PublicationLike> existingLike = likeRepository.findByPublicationAndUserAccount(publication, userAccount);

        if (existingLike.isPresent()) {
            // Si existe, quitar el "me gusta"
            likeRepository.delete(existingLike.get());
        } else {
            // Si no existe, crear un nuevo "me gusta"
            PublicationLike publicationLike = new PublicationLike();
            publicationLike.setPublication(publication);
            publicationLike.setUserAccount(userAccount);
            publicationLike.setLikeDate(LocalDateTime.now());
            likeRepository.save(publicationLike);
        }
    }


    // Obtener la lista de usuarios que han dado "me gusta" a una publicación
    public List<UserDTO> getUsersWhoLikedPublication(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        return likeRepository.findByPublication(publication)
                .stream()
                .map(publicationLike -> modelMapper.map(publicationLike.getUserAccount(), UserDTO.class))
                .collect(Collectors.toList());
    }

    // Método para obtener la cantidad de "me gusta" en una publicación
    public int getLikeCountByPublication(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));
        return likeRepository.countByPublication(publication);
    }


    // Obtener todos los likes dados
    public List<LikeDTO> getAllLikes() {
        List<PublicationLike> publicationLikes = likeRepository.findAll();
        return publicationLikes.stream()
                .map(publicationLike -> modelMapper.map(publicationLike, LikeDTO.class))
                .collect(Collectors.toList());
    }

    // Añade este metodo para quitar "me gusta" de una publicación
    public void unlikePublication(Long publicationId, String userEmail) {
        // Buscar la publicación por su ID
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        // Obtener el usuario autenticado
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);
        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        // Buscar el "me gusta" existente
        Optional<PublicationLike> existingLike = likeRepository.findByPublicationAndUserAccount(publication, userAccount);

        if (existingLike.isPresent()) {
            // Si existe, eliminar el "me gusta"
            likeRepository.delete(existingLike.get());
        } else {
            throw new ResourceNotFoundException("El usuario no ha dado 'me gusta' a esta publicación.");
        }
    }

}
