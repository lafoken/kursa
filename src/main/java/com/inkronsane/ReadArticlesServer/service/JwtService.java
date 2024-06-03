package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.entity.User;
import com.inkronsane.ReadArticlesServer.security.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Service for generation and verification of JWT tokens.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Service
public class JwtService {

   private final SecretKey key;
   private static final long jwtLifetime = 8640000;

   /**
    * Constructor for initializing the secret key.
    */
   public JwtService() {
      String secretString = "BSVBSHBVSBVSHBDXXXYTDT76587575785HJFVTYFFGHVGCFMUD";
      byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
      this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
   }

   /**
    * Generates a JWT token for the user.
    *
    * @param userDetails the user object for which the token is generated
    * @return JWT token
    */
   public String generateToken(CustomUserDetails userDetails) {
      return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtLifetime))
        .signWith(key)
        .compact();
   }

   /**
    * Generates an updated JWT token with passed stamps.
    *
    * @param claims stamps for the token
    * @param userDetails the user object for which the token is generated
    * @return the updated JWT token
    */
   public String generateRefreshToken(HashMap<String, Object> claims, CustomUserDetails userDetails) {
      return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtLifetime))
        .signWith(key)
        .compact();
   }

   /**
    * Extracts the username from the JWT token.
    *
    * @param token JWT token
    * @return username
    */
   public String extractUsername(String token) {
      return extractClaims(token, Claims::getSubject);
   }

   private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
      return claimsTFunction.apply(
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
   }

   /**
    * Checks if the JWT token is valid for the specified user.
    *
    * @param token JWT token
    * @param userDetails the user object
    * @return true if the token is valid, false otherwise
    */
   public boolean isTokenValid(String token, CustomUserDetails userDetails) {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   private boolean isTokenExpired(String token) {
      return extractClaims(token, Claims::getExpiration).before(new Date());
   }
}