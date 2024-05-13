package com.inkronsane.ReadArticlesServer.dto.search;


import com.fasterxml.jackson.annotation.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import java.util.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Search class
 * This class is used for a more sophisticated search for articles
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CommentSearch {
   private String author;
   private Long authorId;
   private Long articleId;
   private String content;
}
