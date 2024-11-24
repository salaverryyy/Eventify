package com.eventos.recuerdos.eventify_project.securityconfig.domain;

import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${my.awesome.secret}")
    private String jwtSigningKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        // Llama al método de abajo con los claims extra
        Map<String, Object> extraClaims = new HashMap<>();

        // Supón que `userDetails` es una instancia de `UserAccount`
        if (userDetails instanceof UserAccount) {
            UserAccount user = (UserAccount) userDetails;
            extraClaims.put("userId", user.getId());  // Agrega el userId como claim
        }

        return generateToken(extraClaims, userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)  // Añade los claims extra
                .setSubject(userDetails.getUsername())  // Aquí el subject sigue siendo el email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Configura el tiempo de expiración
                .signWith(SignatureAlgorithm.HS512, jwtSigningKey)  // Usa la clave secreta para firmar el token
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }



    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //metodo para extraer el userId del token
    public Long extractUserIdFromToken(String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSigningKey) // Usa tu clave secreta configurada
                .parseClaimsJws(jwt)
                .getBody();

        // Suponiendo que el userId está almacenado como un claim llamado "userId"
        return Long.parseLong(claims.get("userId").toString());
    }

}
