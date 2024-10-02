package com.eventos.recuerdos.eventify_project.memory.dto;

import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class MemoryWithPublicationsDTO {
    private Long id;
    private String memoryName;
    private String description;
    private LocalDateTime memoryCreationDate;
    private List<PublicationDTO> publications;
}
//Mostrar un recuerdo con todas las publicaciones (fotos y videos) asociadas.