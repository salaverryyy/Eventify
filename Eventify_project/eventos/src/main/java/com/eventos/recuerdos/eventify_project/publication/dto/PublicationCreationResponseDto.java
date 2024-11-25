package com.eventos.recuerdos.eventify_project.publication.dto;

import com.eventos.recuerdos.eventify_project.publication.domain.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicationCreationResponseDto {

    private Long id;  // El ID de la publicación (se puede omitir al crear una nueva)

    @NotNull(message = "El tipo de archivo no puede ser nulo.")
    private FileType type;  // FOTO o VIDEO

    @NotBlank(message = "La URL no puede estar en blanco.")

    private String fileUrl;  // URL del archivo (en AWS S3)

    private String description;  // Descripción de la publicación

    private LocalDateTime publicationDate;  // Fecha y hora de la publicación


}
