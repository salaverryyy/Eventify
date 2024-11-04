package com.eventos.recuerdos.eventify_project.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String username;
}

//Transferir información básica de los usuarios,
// como nombre, email y fecha de creación del perfil
