package com.inkronsane.ReadArticlesServer.repository;


import com.inkronsane.ReadArticlesServer.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
/**
 * Repository class
 * This class is used for more convenient work with the database
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

   @Query("SELECT t FROM Tag t WHERE t.name = :name")
   Optional<Tag> findByName(String name);
}
