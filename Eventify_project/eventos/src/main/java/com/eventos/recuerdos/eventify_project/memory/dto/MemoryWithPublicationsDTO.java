package com.eventos.recuerdos.eventify_project.memory.dto;

import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class MemoryWithPublicationsDTO {
    @Valid
    private MemoryDTO memory;
    @Valid
    private List<PublicationDTO> publications;

    public MemoryWithPublicationsDTO(MemoryDTO memory, List<PublicationDTO> publications) {
        this.memory = memory;
        this.publications = publications;
    }
}
//Mostrar un recuerdo con todas las publicaciones (fotos y videos) asociadas.