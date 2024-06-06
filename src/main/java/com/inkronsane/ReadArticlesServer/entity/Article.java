package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"articleTags"})
@Entity
@Table(name = "articles")
/**
 * The essence of the article
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */

public class Article extends AuditableEntity<Long> implements Comparable<Article> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   /**
    Unique identifier
    */
   private Long id;

   private String title;
   private String content;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "author_id")
   private User author;

   @ManyToMany(cascade = CascadeType.REMOVE)
   @JoinTable(
     name = "article_tags",
     joinColumns = @JoinColumn(name = "article_id"),
     inverseJoinColumns = @JoinColumn(name = "tag_id"))
   private Set<Tag> articleTags;

   @Override
   public int compareTo(Article o) {
      return o.getId().compareTo(id);
   }
}