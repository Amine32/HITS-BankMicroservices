package ru.tsu.hits.loan_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            //TODO add exception management
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);  // Extract the user ID claim
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

        List<?> roles = claims.getBody().get("authorities", List.class);
        if (roles == null) {
            throw new JwtException("Claims 'authorities' is missing");
        }

        return roles.stream()
                .filter(obj -> obj instanceof String)
                .map(obj -> new SimpleGrantedAuthority((String) obj))
                .collect(Collectors.toList());
    }
}
