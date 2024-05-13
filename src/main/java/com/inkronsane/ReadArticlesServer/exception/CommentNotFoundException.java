package com.inkronsane.ReadArticlesServer.exception;

/**
 * Exception
 * This class is used to handle exceptions.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CommentNotFoundException extends RuntimeException{

   public CommentNotFoundException(Long id) {
      super("Comment with " +" '" + id + "' id not found.");
   }
}
