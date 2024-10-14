package com.eventos.recuerdos.eventify_project.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDate userCreationDate;

}

//Transferir información básica de los usuarios,
// como nombre, email y fecha de creación del perfil
