package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import java.io.*;
import java.time.*;
import lombok.*;

@Getter
@Setter
@MappedSuperclass
/**
 * The essence of the audit
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {
   private Instant createdAt;
   private Instant updatedAt;
   @PrePersist
   protected void prePersist(){
      if(createdAt == null){}{
         createdAt = Instant.now();
      }
      updatedAt = Instant.now();
   }
}