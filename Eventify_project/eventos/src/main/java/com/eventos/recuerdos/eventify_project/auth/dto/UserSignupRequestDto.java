package com.eventos.recuerdos.eventify_project.auth.dto;


import lombok.Data;

@Data
public class UserSignupRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
