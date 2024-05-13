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
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>  {

//   @EntityGraph("User")
   /**
    * User search method by username
    * This method takes a parameter and returns a result.
    * @param username username of the user
    * @return optional user
    */
@EntityGraph(attributePaths = {"roles"})
  public Optional<User> findByUsername(String username);
}
