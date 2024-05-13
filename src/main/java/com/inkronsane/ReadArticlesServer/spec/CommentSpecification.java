package com.inkronsane.ReadArticlesServer.spec;

import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
/**
 * Specification for filtering comments.
 * This class is used to create conditions for searching comments based on various criteria.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CommentSpecification implements Specification<Comment> {

   transient CommentSearch commentSearch;

   /**
    * Generates a predicate for finding comments based on the specified criteria.
    *
    * @param root the root object of the criteria
    * @param query query criteria object
    * @param criteriaBuilder criteria builder
    * @return predicate to search for comments
    */
   @Override
   public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query,
     CriteriaBuilder criteriaBuilder) {
      Predicate predicate = criteriaBuilder.conjunction();

      if (commentSearch.getArticleId() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.equal(root.get("article").get("id"), commentSearch.getArticleId()));
      }

      if (commentSearch.getAuthorId() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.equal(root.get("author").get("id"), commentSearch.getAuthorId()));
      }

      if (commentSearch.getContent() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.like(root.get("content"), "%" + commentSearch.getContent() + "%"));
      }
      return predicate;
   }
}