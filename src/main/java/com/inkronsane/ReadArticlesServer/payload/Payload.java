package com.inkronsane.ReadArticlesServer.payload;


import com.fasterxml.jackson.annotation.*;
import java.util.*;
import lombok.*;
import org.springframework.validation.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Payload class
 * This class is used for more convenient sending of data to the web server
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public abstract class Payload {

   private int statusCode;
   private String error;
   private String message;
   private List<ObjectError> errors;
   public void setErrorInfo(int statusCode, String error) {
this.statusCode = statusCode;
this.error = error;
   }
   public void setInfo(int statusCode, String message) {
      this.statusCode = statusCode;
      this.message = message;
   }
}
