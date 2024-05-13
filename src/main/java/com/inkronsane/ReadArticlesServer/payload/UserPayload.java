package com.inkronsane.ReadArticlesServer.payload;


import com.fasterxml.jackson.annotation.*;
import com.inkronsane.ReadArticlesServer.dto.*;
import java.util.*;
import lombok.*;
import org.springframework.data.domain.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Payload class
 * This class is used for more convenient sending of data to the web server
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class UserPayload extends Payload {
   private int code;
   private UserDto dto;
   private List<UserDto> dtos;
}
