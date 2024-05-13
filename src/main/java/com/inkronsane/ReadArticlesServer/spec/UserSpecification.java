package com.inkronsane.ReadArticlesServer.spec;


import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
/**
 * Specification for user filtering.
 * This class is used to create conditions for searching comments based on various criteria.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class UserSpecification implements Specification<User> {

   transient UserSearch userSearch;
   /**
    * Generates a predicate for searching users according to the specified criteria.
    *
    * @param root the root object of the criteria
    * @param query query criteria object
    * @param criteriaBuilder criteria builder
    * @return predicate to search for comments
    */
   @Override
   public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
     CriteriaBuilder criteriaBuilder) {
      Predicate predicate = criteriaBuilder.conjunction();

      if (userSearch.getUsername() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.equal(root.get("username"), userSearch.getUsername()));
      }

      if (userSearch.getRole() != null) {
         predicate = criteriaBuilder.and(predicate,
           criteriaBuilder.equal(root.get("role"), userSearch.getRole()));
      }
      return predicate;
   }
}