package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import java.util.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = "articles")
@Entity
@Table(name = "tags")
/**
 * The essence of the tag
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class Tag implements Comparable<Tag> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @Column(unique = true)
   private String name;

   @ManyToMany(mappedBy = "articleTags")
   Set<Article> articles;

   @Override
   public int compareTo(Tag o) {
      return o.getName().compareTo(name);
   }
}