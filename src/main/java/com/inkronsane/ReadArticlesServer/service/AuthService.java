package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.*;

@Transactional
@Service
/**
 * User authentication and registration service.
 * This class is responsible for registering new users, authenticating, updating and validating access tokens.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class AuthService {

   @Autowired
   private UserService userService;
   @Autowired
   private RoleService roleService;
   @Autowired
   private JwtService jwtService;
   @Autowired
   private PasswordEncoder passwordEncoder;
   @Autowired
   private AuthenticationManager authenticationManager;
@Autowired
private CustomUserDetailsService customUserDetailsService;
   @Autowired
   private EmailSenderService emailSenderService;
   @Autowired
   private CacheManager cacheManager;
   @Autowired
   private Validator userValidator;
   /**
    * Registers a new user based on the given request.
    *
    * @param request user registration request
    * @return a response that contains information about the registration result
    */
   public UserPayload signUp(UserPayload request) {
      // Validation of entered data
      Errors errors = new BeanPropertyBindingResult(request, "userDto");
      userValidator.validate(request, errors);
      if (errors.hasErrors()) {
         UserPayload response = new UserPayload();
         response.setErrorInfo(500, "Invalid user data");
         response.setErrors(errors.getAllErrors());
         return response;
      }
      // Create a user object and save it
      UserDto userDto = new UserDto(
        null,
        request.getDto().getUsername(),
        request.getDto().getEmail(),
        passwordEncoder.encode(request.getDto().getPassword()),
        request.getDto().getPersonalInfo(),
        roleService.getUserRole().getName(),
        null,
      null
       );
      return userService.createUser(userDto);
   }

   /**
    * Authenticates the user based on the provided request and generates an access token.
    *
    * @param signinRequest user authentication request
    * @return a response that contains the access token and information about the authentication result
    */
   public TokenPayload signIn(UserPayload signinRequest) {
      TokenPayload response = new TokenPayload();
      try {
         // User authentication
         authenticationManager. authenticate(
           new UsernamePasswordAuthenticationToken(
             signinRequest.getDto().getUsername(), signinRequest.getDto().getPassword()
           )
         );
         // Get the user and generate the access token
//         var user = userService.getTrueUserByUsername(signinRequest.getDto().getUsername());
         var user = customUserDetailsService.loadUserByUsername(signinRequest.getDto().getUsername());
         var jwt = jwtService.generateToken(user);
         var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
         // Configure the response
         response.setStatusCode(200);
         response.setMessage("Successfully Signed In");
         response.setToken(jwt);
         response.setRefreshToken(refreshToken);
         response.setExpirationDate("24Hr");
      } catch (Exception e) {
         response.setStatusCode(500);
         response.setError(e.getMessage());
      }
      return response;
   }

   /**
    * Updates the access token based on the provided update token.
    *
    * @param refreshTokenRequest access token refresh request
    * @return a response that contains the updated access token and information about the result of the update
    */
   public TokenPayload refreshToken(TokenPayload refreshTokenRequest) {
      TokenPayload response = new TokenPayload();
      try {
         String username = jwtService.extractUsername(refreshTokenRequest.getRefreshToken());
//         var user = userService.getTrueUserByUsername(username);
         var user = customUserDetailsService.loadUserByUsername(username);
         if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
            // Generation of a new access token
            String newToken = jwtService.generateToken(user);
            // Configure the response
            response.setStatusCode(200);
            response.setToken(newToken);
            response.setRefreshToken(refreshTokenRequest.getRefreshToken());
            response.setExpirationDate("24Hr");
            response.setMessage("Successfully Refreshed Token");
         } else {
            // Error if update token is invalid
            response.setStatusCode(401);
            response.setError("Invalid refresh token");
         }
      } catch (Exception e) {
         // Handle the exception
         response.setStatusCode(500);
         response.setError("Internal server error");
      }
      return response;
   }

   /**
    * Checks the confirmation code that was sent to the email and registers a new user.
    * If the code matches what was stored in the cache, the new user will be registered.
    * Otherwise, an error will be returned.
    *
    * @param registrationRequest user registration request
    * @return a response that contains information about the result of checking the confirmation code and registering the user
    */
   public UserPayload codeChecking(UserPayload registrationRequest) {
      UserPayload response = new UserPayload();
      int code = isEmailCodeCached(registrationRequest.getDto().getEmail());
      if (code != 0) {
         if (code == registrationRequest.getCode()) {
            return signUp(registrationRequest);
         } else {
            response.setStatusCode(400);
            response.setMessage("incorrect code or email.");
            return response;
         }
      } else {
         int sendedCode = generateVerificationCode();
         emailSenderService.sendRegistrationEmail(registrationRequest.getDto().getEmail(), sendedCode);
         cacheEmailCode(registrationRequest.getDto().getEmail(), sendedCode);
         response.setStatusCode(200);
         response.setMessage("Code was sent.");
      }
      return response;
   }

   /**
    * Checks whether the verification code for the specified email is cached.
    *
    * @param email the email address for which the presence of the confirmation code in the cache is checked
    * @return the confirmation code if cached, or 0 if not found
    */
   private int isEmailCodeCached(String email) {
      Cache cache = cacheManager.getCache("VerificationCodes");
      if (cache != null && cache.get(email) != null) {
         return cache.get(email, Integer.class);
      }
      return 0;
   }

   /**
    * Stores the verification code for the specified email in the cache.
    *
    * @param email email address for which the verification code is stored
    * @param code the confirmation code that is stored in the cache
    */
   private void cacheEmailCode(String email, int code) {
      Cache cache = cacheManager.getCache("VerificationCodes");
      if (cache != null) {
         cache.put(email, code);
      }
   }

   /**
    * Generates a random verification code for email.
    *
    * @return the generated random confirmation code
    */
   private static Integer generateVerificationCode() {
      return (int) (Math.random() * 900000 + 100000);
   }
}