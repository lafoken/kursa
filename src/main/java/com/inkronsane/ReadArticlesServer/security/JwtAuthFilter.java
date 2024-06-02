package com.inkronsane.ReadArticlesServer.security;


import com.inkronsane.ReadArticlesServer.entity.User;
import com.inkronsane.ReadArticlesServer.repository.*;
import com.inkronsane.ReadArticlesServer.service.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.validation.constraints.*;
import java.io.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

   @Autowired
   private final JwtService jwtService;
   @Autowired
   private final UserRepository userService;

   @Override
   protected void doFilterInternal(
     @NotNull HttpServletRequest request,
     @NotNull HttpServletResponse response,
     @NotNull FilterChain filterChain)
     throws ServletException, IOException {
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String username;
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
      }
      jwt = authHeader.substring(7);
      username = jwtService.extractUsername(jwt);
      if (username != null || SecurityContextHolder.getContext().getAuthentication() == null) {
         User userDetails = userService.findByUsername(username).orElseThrow();
         if (jwtService.isTokenValid(jwt, userDetails)) {
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken token =
              new UsernamePasswordAuthenticationToken(
                userDetails.getId(), null, userDetails.getRoles());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(token);
            SecurityContextHolder.setContext(securityContext);
         }
      }
      filterChain.doFilter(request, response);
   }
}