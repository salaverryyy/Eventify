package com.eventos.recuerdos.eventify_project.publication.domain;

import com.eventos.recuerdos.eventify_project.exception.ResourceNotFoundException;
import com.eventos.recuerdos.eventify_project.like.dto.LikeDTO;
import com.eventos.recuerdos.eventify_project.like.infrastructure.LikeRepository;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.memory.infrastructure.MemoryRepository;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationCreationResponseDto;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import com.eventos.recuerdos.eventify_project.publication.infrastructure.PublicationRepository;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
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
    private MemoryRepository memoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public PublicationDTO getPublicationById(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));
        return modelMapper.map(publication, PublicationDTO.class);
    }

    public PublicationCreationResponseDto createPublication(Long memoryId, MultipartFile file, String description, String userEmail) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es requerido para crear una publicación.");
        }

        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Recuerdo no encontrado con id: " + memoryId));
        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);
        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        Publication publication = new Publication();
        publication.setDescription(description);
        publication.setFileUrl(uploadFileToS3(file));
        publication.setFileType(detectFileType(file));
        publication.setAuthor(userAccount);
        publication.setMemory(memory);
        publication.setPublicationDate(LocalDateTime.now());

        Publication savedPublication = publicationRepository.save(publication);
        return modelMapper.map(savedPublication, PublicationCreationResponseDto.class);
    }

    private String uploadFileToS3(MultipartFile file) {
        return "https://bucket-s3.s3.amazonaws.com/" + file.getOriginalFilename();
    }

    public PublicationCreationResponseDto updatePublication(Long id, MultipartFile file, String description, String userEmail) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));

        UserAccount userAccount = userAccountRepository.findByEmail(userEmail);
        if (userAccount == null) {
            throw new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail);
        }

        if (!publication.getAuthor().getId().equals(userAccount.getId())) {
            throw new SecurityException("No tienes permiso para editar esta publicación.");
        }

        publication.setDescription(description);

        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFileToS3(file);
            publication.setFileUrl(fileUrl);
            publication.setFileType(detectFileType(file));
        }

        publicationRepository.save(publication);
        return modelMapper.map(publication, PublicationCreationResponseDto.class);
    }

    private FileType detectFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("El tipo de contenido es nulo");
        }
        if (contentType.startsWith("image")) {
            return FileType.FOTO;
        } else if (contentType.startsWith("video")) {
            return FileType.VIDEO;
        } else {
            throw new IllegalArgumentException("Tipo de archivo no soportado: " + contentType);
        }
    }

    public void deletePublication(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + id));
        publicationRepository.delete(publication);
    }

    public List<LikeDTO> getLikesByPublication(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publicación no encontrada con ID: " + publicationId));

        return publication.getPublicationLikes().stream()
                .map(like -> modelMapper.map(like, LikeDTO.class))
                .collect(Collectors.toList());
    }

    public int getLikeCount(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        return publication.getPublicationLikes().size();
    }
}
