package com.inkronsane.ReadArticlesServer.payload;


import com.fasterxml.jackson.annotation.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Payload class
 * This class is used for more convenient sending of data to the web server
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class TokenPayload extends Payload {
private Long uid;
   private String token;
   private String refreshToken;
   private String expirationDate;
}
