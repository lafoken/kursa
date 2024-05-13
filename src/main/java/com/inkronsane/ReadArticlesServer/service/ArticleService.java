package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.exception.*;
import com.inkronsane.ReadArticlesServer.mapper.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.repository.*;
import com.inkronsane.ReadArticlesServer.spec.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.*;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
/**
 * Articles service.
 * This class is used to work with articles: creation, retrieval, editing and deletion.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class ArticleService {

   private final ArticleRepository articleRepository;
   private final UserService userService;
   private final ArticleMapper articleMapper;
   private final TagService tagService;
   @Autowired
   private Validator articleValidator;

   /**
    * Creates a new article based on the given query.
    *
    * @param request request to create an article
    * @return a response that contains information about the creation result
    */
   public ArticlePayload create(ArticlePayload request) {
      ArticlePayload response = new ArticlePayload();
      try {
         Article article = articleMapper.mapToArticle(request.getDto());
         article.setArticleTags(setArticleTags(request.getDto().getTags()));
         articleRepository.save(article);
         response.setInfo(200, "Article created successfully");
      } catch (Exception e) {
         log.error("Error occurred for {} :", request.getDto().getId(), e.getMessage());
         response.setErrorInfo(500, "Failed to create article: " + e.getMessage());
      }
      return response;
   }

   /**
    * Returns all articles from the specified page.
    *
    * @param page information about the page
    * @return a response containing a list of articles on the specified page
    */
   public ArticlePayload getAll(PageRequest page) {
      ArticlePayload response = new ArticlePayload();
      try {
         response.setDtos(
           articleRepository.findAll(page).getContent().stream()
             .map(articleMapper::mapToDto)
             .collect(Collectors.toList()));
         response.setStatusCode(200);
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch articles: " + e.getMessage());
      }
      return response;
   }

   /**
    * Returns the article with the specified ID.
    *
    * @param id the id of the article
    * @return a response that contains information about the article with the specified ID
    */
   @Cacheable(cacheNames = "articles", key = "#id")
   public ArticlePayload getById(Long id) {
      ArticlePayload response = new ArticlePayload();
      try {
         Optional<Article> articleOptional = articleRepository.findById(id);
         if (articleOptional.isPresent()) {
            ArticleDto article = articleMapper.mapToDto(articleOptional.get());
            response.setStatusCode(200);
            response.setDto(article);
         } else {
            response.setErrorInfo(404, "Article not found");
            throw new ArticleNotFoundException(id);
         }
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch article: " + e.getMessage());
      }
      return response;
   }

   /**
    * Returns a list of articles that match the given search parameters.
    *
    * @param articleSearch article search parameters
    * @param page information about the page
    * @return a response containing a list of articles matching the search criteria
    */
   public ArticlePayload searchArticles(ArticleSearch articleSearch, PageRequest page) {
      ArticlePayload response = new ArticlePayload();
      try {
         if(articleSearch.getAuthor() != null){
            articleSearch.setAuthorId(userService.getTrueUserByUsername(articleSearch.getAuthor()).getId());
         }
         if(articleSearch.getTags() != null){
            Set<String> tagNames = Set.of(articleSearch.getTags().split("\\+"));
            articleSearch.setTagEntities(setArticleTags(tagNames));
         }
         Specification<Article> spec = new ArticleSpecification(articleSearch);
         response.setDtos(articleRepository.findAll(spec, page).stream()
           .map(articleMapper::mapToDto)
           .collect(Collectors.toList()));
         response.setInfo(200, "Articles found successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to search articles: "+ e.getMessage());
      }
      return response;
   }

   /**
    * Updates an existing article based on a given query.
    *
    * @param request request to update the article
    * @return a response containing information about the update result
    */
   @CacheEvict(cacheNames = "articles", key = "#request.dto.id")
   @CachePut(cacheNames = "articles", key = "#request.dto.id")
   public ArticlePayload update(ArticlePayload request) {
      ArticlePayload response = new ArticlePayload();
      try {
         Optional<Article> optionalArticle =
           articleRepository.findById(request.getDto().getId());
         if (optionalArticle.isPresent()) {
            Errors errors = new BeanPropertyBindingResult(request, "articlePayload");
            articleValidator.validate(request, errors);
            if (errors.hasErrors()) {
               response.setErrorInfo(500, "Invalid article data");
               response.setErrors(errors.getAllErrors());
               return response;
            }
            Article article = optionalArticle.get();
            article.setTitle(request.getDto().getTitle());
            article.setContent(request.getDto().getContent());
            article.setArticleTags(setArticleTags(request.getDto().getTags()));
            articleRepository.save(article);
            response.setInfo(200, "Article updated successfully");
         } else {
            response.setInfo(404, "Article not found");
         }
      } catch (Exception e) {
         log.error("Error occurred for {} :", request.getDto().getId(), e.getMessage());
         response.setErrorInfo(500, "Failed to update article: " + e.getMessage());
      }
      return response;
   }

   /**
    * Deletes the article with the specified ID.
    *
    * @param id the id of the article
    * @return a response that contains information about the result of the deletion
    */
   @CacheEvict(cacheNames = "articles", key = "#id")
   public ArticlePayload delete(Long id) {
      ArticlePayload response = new ArticlePayload();
      try {
         Optional<Article> optionalArticle = articleRepository.findById(id);
         if (optionalArticle.isPresent()) {
            articleRepository.delete(optionalArticle.get());
            response.setInfo(200, "Article deleted successfully");
         } else {
            response.setInfo(404, "Article not found");
         }
      } catch (Exception e) {
         response.setStatusCode(500);
         response.setError("Failed to delete article: " + e.getMessage());
      }
      return response;
   }

   private Set<Tag> setArticleTags(Set<String> request) {
      return request.stream()
        .map(tagService::getByName)
        .filter(tag -> tag.isPresent())
        .map(tag -> tag.get())
        .collect(Collectors.toSet());
   }
}