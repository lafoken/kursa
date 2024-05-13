package com.inkronsane.ReadArticlesServer.dto;


import com.fasterxml.jackson.annotation.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import java.time.*;
import java.util.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO
 * This class is used for more convenient data mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class UserDto {

   private Long id;
   private String username;
   private String email;
   private String password;
   private PersonalInfoDto personalInfo;
   private String role;
   private Set<ArticleDto> userArticles;
   private Set<CommentDto> userComments;
}
