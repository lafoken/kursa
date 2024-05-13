package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraph(
  name = "Comment",
  attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("article")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "comments")
/**
 * The essence of the comment
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class Comment extends AuditableEntity<Long> implements Comparable<Comment> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   /**
    Unique identifier
    */
   private Long id;

   @ManyToOne
   @JoinColumn(name = "article_id", nullable = false, updatable = false)
   private Article article;

   @ManyToOne
   @JoinColumn(name = "author_id", nullable = false, updatable = false)
   private User author;

   private String content;

   @Override
   public int compareTo(Comment o) {
      return o.getId().compareTo(id);
   }
}