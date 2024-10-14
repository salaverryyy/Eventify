package com.eventos.recuerdos.eventify_project.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SigninRequest {
    private String email;
    private String password;

    public SigninRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
