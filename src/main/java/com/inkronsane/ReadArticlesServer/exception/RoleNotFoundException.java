package com.inkronsane.ReadArticlesServer.exception;

/**
 * Exception
 * This class is used to handle exceptions.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class RoleNotFoundException extends RuntimeException{
   public RoleNotFoundException(String roleName) {
      super("Role '" + roleName + "' not found.");
   }
}
