package com.eventos.recuerdos.eventify_project.publication.domain;

import com.eventos.recuerdos.eventify_project.comment.domain.Comment;
import com.eventos.recuerdos.eventify_project.comment.dto.CommentDTO;
import com.eventos.recuerdos.eventify_project.comment.infrastructure.CommentRepository;
import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.like.domain.Like;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.like.infrastructure.LikeRepository;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    //metodos
    //Obtener los detalles de una publicación por su ID.
    public PublicationDTO getPublicationById(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));
        return modelMapper.map(publication, PublicationDTO.class);
    }

    // Método para crear una publicación
    public PublicationDTO createPublication(Long memoryId, MultipartFile file, String description, Long userId) {
        // Buscar el recuerdo (Memory) por ID y asegurarse de que existe
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Recuerdo no encontrado con id: " + memoryId));

        // Buscar el usuario por ID y asegurarse de que existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        // Crear una nueva publicación (Publication)
        Publication publication = new Publication();
        publication.setDescription(description);
        publication.setFileUrl(file.getOriginalFilename());  // Aquí puedes manejar la lógica de subida a AWS S3 u otro almacenamiento
        publication.setFileType(detectFileType(file));  // Detectar si el archivo es una imagen o video
        publication.setUser(user);  // Asociar el usuario a la publicación
        publication.setMemory(memory);  // Asociar la publicación al recuerdo
        publication.setPublicationDate(LocalDateTime.now());  // Establecer la fecha de publicación actual

        // Guardar la publicación en la base de datos
        Publication savedPublication = publicationRepository.save(publication);

        // Mapear la publicación guardada a DTO y devolverla
        return modelMapper.map(savedPublication, PublicationDTO.class);
    }

    // Método para detectar el tipo de archivo (imagen o video)
    private FileType detectFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image")) {
            return FileType.FOTO;
        } else if (contentType != null && contentType.startsWith("video")) {
            return FileType.VIDEO;
        } else {
            throw new IllegalArgumentException("Tipo de archivo no soportado");
        }
    }





    //Editar la descripción o archivo de una publicación
    public PublicationDTO updatePublication(Long id, PublicationDTO publicationDTO) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));

        publication.setDescription(publicationDTO.getDescription());
        publication.setFileType(publicationDTO.getType());

        publicationRepository.save(publication);
        return modelMapper.map(publication, PublicationDTO.class);
    }

    //Eliminar una publicación
    public void deletePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));
        publicationRepository.delete(publication);
    }

    //Dar "me gusta" a una publicación
    public LikeDTO likePublication(Long publicationId, LikeDTO likeDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + publicationId));

        Like like = modelMapper.map(likeDTO, Like.class);
        like.setPublication(publication);

        likeRepository.save(like);
        return modelMapper.map(like, LikeDTO.class);
    }

    //Comentar en una publicación
    public CommentDTO commentOnPublication(Long publicationId, CommentDTO commentDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + publicationId));

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPublication(publication);

        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    //Obtener la lista de usuarios que dieron "me gusta" a una publicación
    public List<LikeDTO> getLikesByPublication(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + publicationId));

        return publication.getLikes().stream()
                .map(like -> modelMapper.map(like, LikeDTO.class))
                .collect(Collectors.toList());
    }

    //Quitar "me gusta" de una publicación
    public void removeLike(Long publicationId, LikeDTO likeDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + publicationId));

        Like like = likeRepository.findById(likeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Like no encontrado con ID: " + likeDTO.getId()));

        likeRepository.delete(like);
    }


    //contadir de likes
    public int getLikeCount(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        return publication.getLikes().size();
    }
}
