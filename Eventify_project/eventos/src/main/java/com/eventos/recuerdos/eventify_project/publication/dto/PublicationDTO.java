package com.eventos.recuerdos.eventify_project.publication.dto;

import com.eventos.recuerdos.eventify_project.publication.domain.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicationDTO {
    @NotNull(message = "El ID no puede ser nulo.")
    private Long id;
    private FileType type;  // FOTO o VIDEO

    @NotBlank(message = "La URL no puede estar en blanco.")
    private String fileUrl;
    private String description;
    private LocalDate publicationDate;
}
//Transferir la información básica de publicaciones (fotos y videos)