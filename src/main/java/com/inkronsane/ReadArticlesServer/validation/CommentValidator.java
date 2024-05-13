package com.inkronsane.ReadArticlesServer.validation;

import com.inkronsane.ReadArticlesServer.payload.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
/**
 * Validator for comments.
 * This class is used to check the correctness of entered data when creating or updating comments.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CommentValidator implements Validator {

   /**
    * Indicates whether the validator supports the given class.
    *
    * @param clazz the class to check
    * @return true if the validator supports the given class; false if not supported
    */
   @Override
   public boolean supports(Class<?> clazz) {
      return CommentPayload.class.equals(clazz);
   }

   /**
    * Checks the object for compliance with the specified rules and adds errors to the error object.
    *
    * @param target the object to check
    * @param errors object where errors will be added
    */
   @Override
   public void validate(Object target, Errors errors) {
      CommentPayload content = (CommentPayload) target;

      // Validation of the "content" field: checking for nullity
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dto.content", "NotEmpty");

      // Validation of the "content" field: check for length
      if (content.getDto().getContent().length() < 5) {
         errors.rejectValue("dto.content", "Size.commentPayload.content");
      }
   }
}