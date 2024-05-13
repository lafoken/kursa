package com.inkronsane.ReadArticlesServer.validation;

import com.inkronsane.ReadArticlesServer.payload.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
/**
 * Validator for users.
 * This class is used to check the correctness of entered data when registering a new user.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class UserValidator implements Validator {

   /**
    * Indicates whether the validator supports the given class.
    *
    * @param clazz the class to check
    * @return true if the validator supports the given class; false if not supported
    */
   @Override
   public boolean supports(Class<?> clazz) {
      return UserPayload.class.equals(clazz);
   }

   /**
    * Checks the object for compliance with the specified rules and adds errors to the error object.
    *
    * @param target the object to check
    * @param errors object where errors will be added
    */
   @Override
   public void validate(Object target, Errors errors) {
      UserPayload userPayload = (UserPayload) target;

      // Check for empty fields
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dto.username", "NotEmpty");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dto.email", "NotEmpty");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dto.password", "NotEmpty");

      // Check the length of username
      if (userPayload.getDto().getUsername().length() < 6) {
         errors.rejectValue("dto.username", "Size.userForm.username");
      }

      // Check email format
      if (!userPayload.getDto().getEmail().matches("^(?!.*gmail.com$)")) {
         errors.rejectValue("dto.email", "Email.userForm.email");
      }

      // Check password length
      if (userPayload.getDto().getPassword().length() < 10) {
         errors.rejectValue("dto.password", "Size.userForm.password");
      }
   }
}