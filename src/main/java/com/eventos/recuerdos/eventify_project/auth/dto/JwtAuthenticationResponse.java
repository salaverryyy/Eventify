package com.eventos.recuerdos.eventify_project.auth.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private Long userId;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token,Long userId) {

        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
