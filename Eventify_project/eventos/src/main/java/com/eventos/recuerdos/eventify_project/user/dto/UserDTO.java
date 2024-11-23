package com.eventos.recuerdos.eventify_project.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private String username; // Este debe ser el nombre de usuario real
    private String email;
    private String firstName;
    private String lastName;

}


//Transferir información básica de los usuarios,
// como nombre, email y fecha de creación del perfil
