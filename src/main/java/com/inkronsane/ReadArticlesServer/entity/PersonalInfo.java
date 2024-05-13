package com.inkronsane.ReadArticlesServer.entity;


import jakarta.persistence.*;
import java.io.*;
import java.time.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
/**
 * user nested class
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class PersonalInfo implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;

   private String firstName;
   private String lastName;
   private LocalDate birthDate;
   private String info;
}