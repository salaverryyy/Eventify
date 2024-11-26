package com.eventos.recuerdos.eventify_project.publication.dto;

import com.eventos.recuerdos.eventify_project.comment.dto.CommentDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class DetailedPublicationDTO {
    private Long id;
    private String type;  // FOTO o VIDEO
    private String fileUrl;
    private String description;
    private LocalDate publicationDate;
    private List<CommentDTO> comments;
    private int likesCount;
}
//Proporcionar detalles completos de una
// publicación, incluyendo comentarios y la cantidad de "me gusta".