package com.inkronsane.ReadArticlesServer.exception;
/**
 * Exception
 * This class is used to handle exceptions.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class ArticleNotFoundException extends RuntimeException{

   public ArticleNotFoundException(Long id) {
      super("Article with '" + id + "' id not found");
   }
}
