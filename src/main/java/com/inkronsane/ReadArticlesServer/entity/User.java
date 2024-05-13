package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;
import org.springframework.data.domain.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles", "articles", "comments"})

@EqualsAndHashCode(of = "username", callSuper = false)
@Entity
@Table(name = "users")
/**
 * User entity
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class User extends AuditableEntity<Long> implements Comparable<User> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   /**
    Unique identifier
    */
   private Long id;

   @Column(unique = true)
   private String username;

   @Column(unique = true)
   private String email;

   private String password;
   private PersonalInfo personalInfo;

   @NotEmpty
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
     name = "user_role",
     joinColumns = @JoinColumn(name = "user_id"),
     inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> roles;
   @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Article> articles;

   @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Comment> comments;

   @Override
   public int compareTo(User o) {
      return username.compareTo(o.username);
   }
}