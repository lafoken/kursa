package com.inkronsane.ReadArticlesServer.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ArticleDto {

   private Long id;
   private String title;
   private String content;
   private String author;
   private Long authorId;
   private Set<String> tags;
}
