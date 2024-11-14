package com.eventos.recuerdos.eventify_project.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    @Size(min = 1, max = 50)
    private String firstName;
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 50)
    private String username;
    @NotNull
    private String email;
    public UserDTO() {
    }

    public UserDTO(String lastName, String username, String firstName) {
        this.lastName = lastName;
        this.username = username;
        this.firstName = firstName;
    }
}

//Transferir información básica de los usuarios,
// como nombre, email y fecha de creación del perfil
