package com.eventos.recuerdos.eventify_project.publication.dto;

import com.eventos.recuerdos.eventify_project.publication.domain.TipoArchivo;

import java.time.LocalDate;

public class PublicationDTO {
    private Long id;
    private TipoArchivo type;  // FOTO o VIDEO
    private String fileUrl;
    private String description;
    private LocalDate publicationDate;
}
//Transferir la información básica de publicaciones (fotos y videos)