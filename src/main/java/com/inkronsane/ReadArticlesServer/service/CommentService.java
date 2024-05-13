package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.exception.*;
import com.inkronsane.ReadArticlesServer.mapper.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.repository.*;
import com.inkronsane.ReadArticlesServer.spec.*;
import com.inkronsane.ReadArticlesServer.validation.*;
import java.util.*;
import java.util.stream.*;
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
@AllArgsConstructor
/**
 * Service for working with comments.
 * This class is responsible for creating, getting, updating and deleting comments.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CommentService {

   private final UserService userService;
   private final ArticleService articleService;
   private final CommentRepository commentRepository;
   private final CommentMapper commentMapper;
   @Autowired
   private final CommentValidator commentValidator;

   /**
    * Creates a new comment based on the given query.
    *
    * @param request request to create a comment
    * @return a response that contains information about the result of creating a comment
    */
   public CommentPayload create(CommentPayload request) {
      CommentPayload response = new CommentPayload();
      try {
         // Checking the availability of the article
         if (articleService.getById(request.getDto().getArticleId()) != null) {
            Errors errors = new BeanPropertyBindingResult(request, "commentPayload");
            // Validation of entered data
            commentValidator.validate(request, errors);
            if (errors.hasErrors()) {
               response.setErrorInfo(400, "Invalid comment data");
               return response;
            }
            // Create and save the comment
            var entity = commentMapper.mapToEntity(request.getDto());
            entity.setAuthor(userService.getTrueUserById(request.getDto().getAuthorId()));
            commentRepository.save(entity);
            response.setInfo(200, "Comment created successfully");
         } else {
            response.setErrorInfo(400, "Article does not exist");
         }
      } catch (Exception e) {
         log.error("Error occurred for {} :", request.getDto().getId(), e.getMessage());
         response.setErrorInfo(500, "Failed to create comment: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets all comments.
    *
    * @param page page parameters
    * @return a response containing all comments
    */
   public CommentPayload getAll(PageRequest page) {
      CommentPayload response = new CommentPayload();
      try {
         // Get all comments
         response.setDtos(
           commentRepository.findAll(page).getContent().stream()
             .map(commentMapper::mapToDto)
             .toList());
         response.setInfo(200, "Comments fetched successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch comments: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets a comment by its ID.
    *
    * @param id comment ID
    * @return a response containing a comment and information about the result of the retrieval
    */
   @Cacheable(cacheNames = "users", key = "#id")
   public CommentPayload getById(long id) {
      CommentPayload response = new CommentPayload();
      try {
         // Get the comment by its ID
         Optional<Comment> entity = commentRepository.findById(id);
         if (entity.isPresent()) {
            response.setDto(commentMapper.mapToDto(entity.get()));
            response.setInfo(200, "Comment fetched successfully");
         } else {
            response.setErrorInfo(500, "Comment not found");
            throw new CommentNotFoundException(id);
         }
      } catch (Exception e) {
      }
      return response;
   }

   /**
    * Updates a comment by its ID.
    *
    * @param newContent the new content of the comment
    * @param id comment ID
    * @return a response that contains information about the result of updating the comment
    */
   @CacheEvict(cacheNames = "comments", key = "#id")
   @CachePut(cacheNames = "comments", key = "#id")
   public CommentPayload update(CommentPayload newContent, Long id) {
      CommentPayload response = new CommentPayload();
      try {
         Optional<Comment> optionalComment = commentRepository.findById(id);
         if (optionalComment.isPresent()) {
            Errors errors = new BeanPropertyBindingResult(newContent, "commentPayload");
            // Validation of entered data
            commentValidator.validate(newContent, errors);
            if (errors.hasErrors()) {
               response.setErrorInfo(400, "Invalid comment data");
               return response;
            }
            // Update the comment
            Comment comment = optionalComment.get();
            comment.setContent(newContent.getDto().getContent());
            commentRepository.save(comment);
            response.setInfo(200, "Comment updated successfully");
         } else {
            response.setErrorInfo(404, "Comment not found");
         }
      } catch (Exception e) {
         log.error("Error occurred for {} :", id, e.getMessage());
         response.setErrorInfo(500, "Failed to update comment: " + e.getMessage());
      }
      return response;
   }

   /**
    * Search for comments based on specified criteria.
    *
    * @param commentSearch comment search options
    * @param page page parameters
    * @return a response that contains comments matching the given criteria
    */
   public CommentPayload searchComments(CommentSearch commentSearch, PageRequest page) {
      CommentPayload response = new CommentPayload();
      try {
         if (commentSearch.getAuthor() != null) {
            commentSearch.setAuthorId(userService.getTrueUserByUsername(commentSearch.getAuthor()).getId());
         }
         // Forming a specification to search for comments
         Specification<Comment> spec = new CommentSpecification(commentSearch);
         // Search for comments
         response.setDtos(commentRepository.findAll(spec, page).stream()
           .map(commentMapper::mapToDto)
           .collect(Collectors.toList()));
         response.setInfo(200, "Comments found successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to search comments: " + e.getMessage());
      }
      return response;
   }

   /**
    * Deletes a comment by its ID.
    *
    * @param id comment ID
    * @return a response that contains information about the result of comment removal
    */
   @CacheEvict(cacheNames = "comments", key = "#id")
   public CommentPayload delete(Long id) {
      CommentPayload response = new CommentPayload();
      try {
         Optional<Comment> optionalComment = commentRepository.findById(id);
         if (optionalComment.isPresent()) {
            // Delete the comment
            commentRepository.delete(optionalComment.get());
            response.setInfo(200, "Comment deleted successfully");
         } else {
            response.setErrorInfo(404, "Comment not found");
         }
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to delete comment: " + e.getMessage());
      }
      return response;
   }
}