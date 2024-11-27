package com.eventos.recuerdos.eventify_project.user.dto;

import com.eventos.recuerdos.eventify_project.memory.dto.MemorySummaryDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserAccountDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private LocalDate userCreationDate;
    private String role;
    private Boolean expired;
    private Boolean locked;
    private Boolean credentialsExpired;
    private Boolean enable;

    private List<MemorySummaryDto> memories; // Listado reducido de Memories sin referencias circulares

}