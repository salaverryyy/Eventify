package com.eventos.recuerdos.eventify_project.comment.domain;

import com.eventos.recuerdos.eventify_project.comment.dto.CommentDTO;
import com.eventos.recuerdos.eventify_project.comment.infrastructure.CommentRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Obtener los detalles de un comentario por ID
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        return modelMapper.map(comment, CommentDTO.class);
    }

    // Crear un nuevo comentario en una publicación
    public CommentDTO createComment(Long publicationId, CommentDTO commentDTO, String userEmail) {
        // Verificar si la publicación existe
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con id: " + publicationId));

        // Verificar si el usuario existe
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);
        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        // Mapear el DTO a la entidad Comment
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPublication(publication); // Asociar con la publicación
        comment.setUserAccount(userAccount); // Asociar con el usuario

        // Asignar la fecha de creación
        comment.setCommentDate(LocalDateTime.now());

        // Guardar el comentario
        comment = commentRepository.save(comment);

        // Retornar el DTO del comentario creado
        return modelMapper.map(comment, CommentDTO.class);
    }

    // Actualizar un comentario por ID
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));

        comment.setContent(commentDTO.getContent());
        comment = commentRepository.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }

    // Eliminar un comentario por ID
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        commentRepository.delete(comment);
    }

    // Obtener todos los comentarios por publicación
    public List<CommentDTO> getCommentsByPublicationId(Long publicationId) {
        List<Comment> comments = commentRepository.findByPublicationId(publicationId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    // Obtener todos los comentarios creados
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }
}
