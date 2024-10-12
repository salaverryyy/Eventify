package com.eventos.recuerdos.eventify_project.securityconfig;

import com.auth0.jwt.JWT;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Extrae el nombre de usuario (o email) del token JWT
    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    // Genera un token JWT con 10 horas de validez
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 horas

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    // Valida el token JWT y actualiza el contexto de seguridad de Spring
    public void validateToken(String token, UserDetails userDetails) throws AuthenticationException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);

            // Verificar si el usuario del token coincide con el UserDetails
            if (!userDetails.getUsername().equals(extractUsername(token))) {
                throw new AuthenticationException("Invalid JWT token: user mismatch") {};
            }
        } catch (JWTVerificationException ex) {
            throw new AuthenticationException("Invalid JWT token", ex) {};
        }
    }

}