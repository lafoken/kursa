package com.inkronsane.ReadArticlesServer.controller;


import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.service.*;
import java.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing articles.
 * Author: Hybalo Oleksandr
 * Date: 2024-05-13
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArticleController {

   /**
    * Service for handling article-related operations.
    */
   @Autowired
   private final ArticleService articleService;

   /**
    * Retrieves all articles.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with ArticlePayload containing the articles.
    */
   @GetMapping("/getAll")
   public ResponseEntity<ArticlePayload> getAllArticles(
     @RequestParam(defaultValue = "0") int page) {
      return ResponseEntity.ok(articleService.getAll(PageRequest.of(page, 10)));
   }

   /**
    * Retrieves a specific article by its ID.
    * @param id The ID of the article to retrieve.
    * @return ResponseEntity with ArticlePayload containing the requested article.
    */
   @GetMapping("/a/{id}")
   public ResponseEntity<ArticlePayload> getArticleById(@PathVariable Long id) {
      return ResponseEntity.ok(articleService.getById(id));
   }

   /**
    * Searches articles based on the provided search criteria.
    * @param articleSearch The search criteria.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with ArticlePayload containing the search results.
    */
   @GetMapping("/search")
   public ResponseEntity<ArticlePayload> searchArticles(ArticleSearch articleSearch, @RequestParam(defaultValue = "0") int page){
      ArticlePayload response = articleService.searchArticles(articleSearch, PageRequest.of(page, 10));
      if(response.getStatusCode() == 200){
         return ResponseEntity.ok(response);
      } else {
         return ResponseEntity.status(response.getStatusCode()).build();
      }
   }

   /**
    * Creates a new article.
    * @param articlePayload The payload containing article data.
    * @param principal The authenticated user.
    * @return ResponseEntity with ArticlePayload containing the created article.
    */
   @PostMapping("/create")
   public ResponseEntity<ArticlePayload> createArticle(@RequestBody ArticlePayload articlePayload, Principal principal) {
      long input = Long.parseLong(principal.getName());
      articlePayload.getDto().setAuthorId(input);
      return ResponseEntity.ok(articleService.create(articlePayload));
   }

   /**
    * Updates an existing article.
    * @param articlePayload The payload containing updated article data.
    * @param principal The authenticated user.
    * @return ResponseEntity with ArticlePayload containing the updated article.
    */
   @PatchMapping("/update")
   public ResponseEntity<ArticlePayload> updateArticle(@RequestBody ArticlePayload articlePayload, Principal principal) {
      long input = Long.parseLong(principal.getName());
      long targetId = articlePayload.getDto().getId();
      if (input == articleService.getById(targetId).getDto().getAuthorId()) {
         return ResponseEntity.ok(articleService.update(articlePayload));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }

   /**
    * Deletes an article by its ID.
    * @param id The ID of the article to delete.
    * @param principal The authenticated user.
    * @return ResponseEntity with ArticlePayload containing the deleted article.
    */
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<ArticlePayload> deleteArticle(@PathVariable Long id, Principal principal) {
      long input = Long.parseLong(principal.getName());
      if (input == articleService.getById(id).getDto().getAuthorId()) {
         return ResponseEntity.ok(articleService.delete(id));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }
}

