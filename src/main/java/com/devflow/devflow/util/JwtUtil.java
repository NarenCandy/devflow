package com.devflow.devflow.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expirationTime;


    private SecretKey getSigningkey (){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(getSigningkey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractUsername(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , java.util.function.Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSigningkey())
        .build()
        .parseClaimsJws(token)
        .getBody();
        
    }  
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);

    }


    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(UserDetails userDetails , String token){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }




}
