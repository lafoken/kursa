package com.inkronsane.ReadArticlesServer.service;


import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.exception.*;
import com.inkronsane.ReadArticlesServer.repository.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

/**
 * Service for operations with user roles.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Transactional
@Service
@RequiredArgsConstructor
public class RoleService {

   private final RoleRepository repository;

   /**
    * Gets the user role from the database.
    *
    * @return the user's role object
    * @throws RoleNotFoundException if role "USER" is not found
    */
   public Role getUserRole() {
      return repository.findRoleByName("USER")
        .orElseThrow(() -> new RoleNotFoundException("USER"));
   }
}