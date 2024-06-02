package com.inkronsane.ReadArticlesServer.repository;


import com.inkronsane.ReadArticlesServer.entity.Role;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
/**
 * Repository class
 * This class is used for more convenient work with the database
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Repository
@SuppressWarnings("JpaQlInspection")

public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findRoleByName(String name);
}
