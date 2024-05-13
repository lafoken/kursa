package com.inkronsane.ReadArticlesServer.controller;


import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user authentication.
 * Author: Hybalo Oleksandr
 * Date: 2024-05-13
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

   @Autowired
   private AuthService authService;
   @Autowired
   private UserService userService;

   /**
    * Endpoint for user registration.
    * @param signUpRequest The payload containing user registration data.
    * @return ResponseEntity with UserPayload containing the registration response.
    */
   @PostMapping("/signup")
   public ResponseEntity<UserPayload> signUp(@RequestBody UserPayload signUpRequest) {
      UserPayload response = authService.codeChecking(signUpRequest);
      if(response.getStatusCode() == 200){
         return ResponseEntity.ok(response);
      } else {
         return ResponseEntity.status(response.getStatusCode()).build();
      }
   }

   /**
    * Endpoint for user authentication.
    * @param signInRequest The payload containing user authentication data.
    * @return ResponseEntity with TokenPayload containing the authentication response.
    */
   @CrossOrigin
   @PostMapping("/signin")
   public ResponseEntity<TokenPayload> signIn(@RequestBody UserPayload signInRequest) {
      TokenPayload response = authService.signIn(signInRequest);
      if(response.getStatusCode() == 200){
         response.setUid(userService.getTrueUserByUsername(signInRequest.getDto().getUsername()).getId());
         return ResponseEntity.ok(response);
      } else {
         return ResponseEntity.status(response.getStatusCode()).build();
      }
   }

   /**
    * Endpoint for refreshing authentication tokens.
    * @param refreshTokenRequest The payload containing the refresh token.
    * @return ResponseEntity with TokenPayload containing the refreshed authentication token.
    */
   @PostMapping("/refresh")
   public ResponseEntity<TokenPayload> refreshToken(@RequestBody TokenPayload refreshTokenRequest) {
      return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
   }
}

