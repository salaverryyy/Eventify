package com.eventos.recuerdos.eventify_project.memory.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MemoryDTO {
    private Long id;
    private String memoryName;
    private String description;
    private LocalDateTime memoryCreationDate;
}
//Maneja la información de los recuerdos (álbumes virtuales), sin
// entrar en detalles de publicaciones o eventos asociados.
