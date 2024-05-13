package com.inkronsane.ReadArticlesServer.spec;

import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import jakarta.persistence.criteria.*;
import java.util.*;
import java.util.stream.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
/**
 * Specification for article filtering.
 * This class is used to create conditions for searching articles by various criteria.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class ArticleSpecification implements Specification<Article> {

   transient ArticleSearch articleSearch;

   /**
    * Generates a predicate for searching articles according to the specified criteria.
    *
    * @param root the root object of the criteria
    * @param query query criteria object
    * @param criteriaBuilder criteria builder
    * @return predicate to find articles
    */
   @Override
   public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query,
     CriteriaBuilder criteriaBuilder) {
      Predicate predicate = criteriaBuilder.conjunction();

      if (articleSearch.getTitle() != null && !articleSearch.getTitle().isEmpty()) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.like(root.get("title"), "%" + articleSearch.getTitle() + "%"));
      }

      if (articleSearch.getAuthorId() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.equal(root.get("author").get("id"), articleSearch.getAuthorId()));
      }

      if (articleSearch.getTagEntities() != null && !articleSearch.getTagEntities().isEmpty()) {
         List<Integer> tagIdsSearch = articleSearch.getTagEntities().stream().map(Tag::getId).collect(Collectors.toList());
         Join<Article, Tag> tagJoin = root.join("articleTags", JoinType.INNER);
         predicate = criteriaBuilder.and(predicate, tagJoin.get("id").in(tagIdsSearch));
      }
      return predicate;
   }
}