package com.example.shopping_app.middleware;

import com.example.shopping_app.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;


    public String generateToken (User user) {
        Map<String, Object> claims = new HashMap<>();
        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getPhoneNumber())
                    .expiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            System.out.println("Cannot create token, error: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaimsFromToken(String token){
        return Jwts.parser().verifyWith(getSigningKey() ).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = this.extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    public boolean isExpireToken(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
}
