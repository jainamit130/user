package com.amit.converse.user.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${converse.jwt.key}")
    private String privateKey;

    @Value("${converse.jwt.expiration}")
    private Long expiration;

    public boolean isTokenValid(String token, UserDetails userDetails){
        final  String username =extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(getExpirationTime()))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Instant getExpirationTime(){
        return Instant.now().plusMillis(expiration);
    }

    public <T> T extractClaim(String token, Function<Claims ,T> claimsResolver) {
        final Claims claims = extractAllClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }

    private Jws<Claims> extractAllClaims(String token) {
        Jws<Claims>claims = Jwts.parser()
                                .verifyWith(getSignInKey()).build()
                                .parseSignedClaims(token);
        return claims;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
