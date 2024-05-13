package com.inkronsane.ReadArticlesServer.dto;


import com.fasterxml.jackson.annotation.*;
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
public class CommentDto {

   private Long id;
   private Long articleId;
   private long authorId;
   String authorUsername;
   String content;
}
