package com.inkronsane.ReadArticlesServer.validation;

import com.inkronsane.ReadArticlesServer.payload.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
/**
 * Validator for articles.
 * This class is used to check the correctness of entered data when creating or updating articles.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class ArticleValidator implements Validator {

   /**
    * Indicates whether the validator supports the given class.
    *
    * @param clazz the class to check
    * @return true if the validator supports the given class; false if not supported
    */
   @Override
   public boolean supports(Class<?> clazz) {
      return ArticlePayload.class.equals(clazz);
   }

   /**
    * Checks the object for compliance with the specified rules and adds errors to the error object.
    *
    * @param target the object to check
    * @param errors object where errors will be added
    */
   @Override
   public void validate(Object target, Errors errors) {
      ArticlePayload articlePayload = (ArticlePayload) target;

      // Checking the correctness of the title of the article
      if (articlePayload.getDto().getTitle() == null || articlePayload.getDto().getTitle().length() < 10 || articlePayload.getDto().getTitle().length() > 60) {
         errors.rejectValue("dto.title", "title.invalid", "Title must be between 10 and 60 characters");
      }

      // Checking the correctness of the content of the article
      if (articlePayload.getDto().getContent() == null || articlePayload.getDto().getContent().length() < 100) {
         errors.rejectValue("dto.content", "content.tooShort", "Content must be at least 100 characters long");
      }
   }
}