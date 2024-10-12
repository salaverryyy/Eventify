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
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final UserService userService;

    // Extrae el nombre de usuario (o email) del token JWT
    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    // Extrae el email del usuario del token JWT
    public String extractUserEmail(String token) {
        return JWT.decode(token).getSubject();
    }

    // Genera un token JWT con 10 horas de validez
    public String generateToken(UserDetails data) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 horas de validez

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(data.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    // Sobrecarga del método para generar un token con el objeto User
    public String generateToken(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        return generateToken(userDetails);
    }

    // Valida el token JWT y actualiza el contexto de seguridad de Spring
    public void validateToken(String token, String userEmail) throws AuthenticationException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);

            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    token,
                    userDetails.getAuthorities()
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);

        } catch (JWTVerificationException ex) {
            throw new AuthenticationException("Token JWT no válido", ex) {};
        }
    }

}
