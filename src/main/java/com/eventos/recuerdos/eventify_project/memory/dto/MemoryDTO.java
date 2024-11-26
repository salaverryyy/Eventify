package com.eventos.recuerdos.eventify_project.memory.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
public class MemoryDTO {

    private Long id;

    @NotBlank(message = "El título no puede estar en blanco.")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres.")
    private String memoryName;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String description;

    private LocalDateTime memoryCreationDate;

    private String albumLink;

    private String accessCode;

    private String coverPhoto;


}

//Maneja la información de los recuerdos (álbumes virtuales), sin
// entrar en detalles de publicaciones o eventos asociados.
