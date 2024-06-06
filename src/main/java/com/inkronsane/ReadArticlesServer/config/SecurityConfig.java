package com.inkronsane.ReadArticlesServer.config;

import com.inkronsane.ReadArticlesServer.security.*;
import com.inkronsane.ReadArticlesServer.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
/**
 * Security configuration class.
 * This class is used to conveniently configure server security.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class SecurityConfig {

   @Autowired
   private CustomUserDetailsService customUserDetailsService;
   @Autowired
   private JwtAuthFilter jwtAuthFilter;

   /**
    * Configures security filters.
    *
    * @param httpSecurity HTTP security configuration object
    * @return a security filter chain object
    * @throws Exception if an error occurred while configuring the filters
    */
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
          request ->
            request
              .requestMatchers("/auth/**")
              .permitAll()
              .requestMatchers("/admin/**")
              .hasAnyAuthority("ADMIN")
              .requestMatchers("/user/**")
              .hasAnyAuthority("USER")
              .requestMatchers("/adminuser/**")
              .hasAnyAuthority("USER", "ADMIN")
              .anyRequest()
              .authenticated())
        .sessionManagement(
          manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
      return httpSecurity.build();
   }

   /**
    * Provides an authentication provider.
    *
    * @return the authentication provider object
    */
   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
      daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
      daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
      return daoAuthenticationProvider;
   }

   /**
    * Provides a password encoder.
    *
    * @return a password encoder object
    */
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   /**
    * Provides an authentication manager.
    *
    * @param authenticationConfiguration authentication configuration
    * @return the authentication manager object
    * @throws Exception if an error occurred while retrieving the authentication manager
    */
   @Bean
   public AuthenticationManager authenticationManager(
     AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }
}