package com.inkronsane.ReadArticlesServer.exception;

/**
 * Exception
 * This class is used to handle exceptions.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class UserNotFoundException extends RuntimeException {

   public UserNotFoundException(Long id) {
      super("User with '" + id + "' id not found.");
   }
}