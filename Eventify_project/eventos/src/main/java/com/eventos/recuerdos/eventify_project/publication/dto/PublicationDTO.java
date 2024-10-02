package com.eventos.recuerdos.eventify_project.publication.dto;

import com.eventos.recuerdos.eventify_project.publication.domain.FileType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicationDTO {
    private Long id;
    private FileType type;  // FOTO o VIDEO
    private String fileUrl;
    private String description;
    private LocalDate publicationDate;
}
//Transferir la información básica de publicaciones (fotos y videos)