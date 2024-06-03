package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.repository.*;
import com.inkronsane.ReadArticlesServer.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Transactional
@Service
/**
 * A class to provide custom user management services.
 * This class is used to load users from the database by their username.
 * Author: ChatGPT
 * Date: 2024|05|13
 */
public class CustomUserDetailsService implements UserDetailsService {
@Lazy
   @Autowired
   private UserRepository userRepository;

   /**
    * Loads a user by their username.
    *
    * @param username username
    * @return user details
    * @throws UsernameNotFoundException if the user with the specified name is not found
    */
   @Override
   public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepository
        .findByUsername(username)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
   }
}