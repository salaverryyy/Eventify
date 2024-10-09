package com.eventos.recuerdos.eventify_project.memory.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
public class MemoryDTO {
    private Long Id;
    @NotBlank(message = "El título no puede estar en blanco.")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres.")
    private String memoryName;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String description;
    private LocalDateTime memoryCreationDate;

    @NotNull(message = "El ID del usuario no puede estar en blanco.")
    private Long userId;
}
//Maneja la información de los recuerdos (álbumes virtuales), sin
// entrar en detalles de publicaciones o eventos asociados.
