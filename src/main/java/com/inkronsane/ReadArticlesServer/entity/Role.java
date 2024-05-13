package com.inkronsane.ReadArticlesServer.entity;



import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Table;
import org.springframework.security.core.*;
@Data
@Table
@EqualsAndHashCode(of = "name")
@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
/**
 * The essence of the role
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class Role implements GrantedAuthority {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   /**
    Unique identifier
    */
   private Long id;

   private String name;

   @Override
   public String getAuthority() {
      return name;
   }
}