package com.inkronsane.ReadArticlesServer.security;

import com.inkronsane.ReadArticlesServer.entity.*;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User detail class.
 * This class is used for convenient handling of security and authorization.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

   // User object
   transient User user;

   /**
    * Returns a collection of user rights.
    *
    * @return a collection of GrantedAuthority objects representing user roles
    */
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return user.getRoles();
   }

   /**
    * Returns the user's password.
    *
    * @return the user's password
    */
   @Override
   public String getPassword() {
      return user.getPassword();
   }

   /**
    * Returns the username.
    *
    * @return username
    */
   @Override
   public String getUsername() {
      return user.getUsername();
   }


   public Long getId(){
      return user.getId();
   }

   /**
    * Checks whether the user account has expired.
    *
    * @return true if the account has not expired, false otherwise
    */
   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   /**
    * Checks whether the user account is locked.
    *
    * @return true if the account is not locked, false otherwise
    */
   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   /**
    * Checks whether the user's password has expired.
    *
    * @return true if the password has not expired, false otherwise
    */
   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   /**
    * Checks if the user account is active.
    *
    * @return true if the account is active, false otherwise
    */
   @Override
   public boolean isEnabled() {
      return true;
   }
}