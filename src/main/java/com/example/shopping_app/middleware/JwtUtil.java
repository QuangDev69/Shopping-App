package com.example.shopping_app.middleware;

import com.example.shopping_app.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256; // 256 bits for HS256

    private SecretKey getSigningKey() {
        try {
            // Derive a secure key from the base secret using PBKDF2 and correct HMAC algorithm
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            // Return a key suitable for HMAC-SHA256
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            System.err.println("Error generating secure key: " + e.getMessage());
            throw new RuntimeException("Failed to generate secure key", e);
        }
    }

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

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken (String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isExpireToken(token));
    }
}
