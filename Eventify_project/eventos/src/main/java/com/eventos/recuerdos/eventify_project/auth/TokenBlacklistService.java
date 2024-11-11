package com.eventos.recuerdos.eventify_project.auth;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    // Añade el token a la lista de tokens invalidados
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Verifica si el token está en la lista de invalidados
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}