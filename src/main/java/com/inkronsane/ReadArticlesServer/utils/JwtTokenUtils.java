//package com.inkronsane.ReadArticlesServer.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.crypto.SecretKey;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
///**
// * Utility class for working with JWT tokens. This class provides the ability to generate JWT tokens
// * for users, as well as retrieve information from JWT tokens, such as username and roles.
// */
//@Component
//@Slf4j
//public class JwtTokenUtils {
//
//   @Value("${jwt.secret}")
//   private String secret; // Secret key for JWT token signing
//   @Value("${jwt.lifetime}")
//   private Duration jwtLifetime; // Expiration duration of JWT tokens
//
//   /**
//    * Generates a JWT token for the specified user.
//    *
//    * @param userDetails User details
//    * @return Generated JWT token
//    */
//   public String generateToken(UserDetails userDetails) {
//      Map<String, Object> claims = new HashMap<>();
//      var rolesList = userDetails.getAuthorities().stream()
//        .map(GrantedAuthority::getAuthority)
//        .toList();
//      claims.put("roles", rolesList);
//
//      LocalDateTime issuedDateTime = LocalDateTime.now();
//      LocalDateTime expiredDateTime = issuedDateTime.plus(jwtLifetime);
//
//      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
//
//      log.info("Jwt Token was created for user: " + userDetails.getUsername());
//      return Jwts.builder()
//        .setClaims(claims)
//        .setSubject(userDetails.getUsername())
//        .setIssuedAt(java.sql.Timestamp.valueOf(issuedDateTime))
//        .setExpiration(java.sql.Timestamp.valueOf(expiredDateTime))
//        .signWith(key)
//        .compact();
//   }
//
//   /**
//    * Retrieves the username from the JWT token.
//    *
//    * @param token JWT token
//    * @return Username
//    */
//   public String getUsername(String token) {
//      return getAllClaimsFromToken(token).getSubject();
//   }
//
//   /**
//    * Retrieves the list of user roles from the JWT token.
//    *
//    * @param token JWT token
//    * @return List of user roles
//    */
//   @SuppressWarnings("unchecked")
//   public List<String> getRoles(String token) {
//      return getAllClaimsFromToken(token).get("roles", List.class);
//   }
//
//   /**
//    * Parses all data from the JWT token.
//    *
//    * @param token JWT token
//    * @return Claims object containing all data from the JWT token
//    */
//   private Claims getAllClaimsFromToken(String token) {
//      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
//      return Jwts.parserBuilder()
//        .setSigningKey(key)
//        .build()
//        .parseClaimsJws(token)
//        .getBody();
//   }
//}