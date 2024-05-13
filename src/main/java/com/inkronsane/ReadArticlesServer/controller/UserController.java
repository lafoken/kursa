package com.inkronsane.ReadArticlesServer.controller;


import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.service.UserService;
import java.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing users.
 * Author: Hybalo Oleksandr
 * Date: 2024-05-13
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

   @Autowired
   private final UserService userService;

   /**
    * Retrieves all users.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with UserPayload containing the users.
    */
   @GetMapping("/getAll")
   public ResponseEntity<UserPayload> getAllUsers(@RequestParam(defaultValue = "0") int page) {
      return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, 10)));
   }

   /**
    * Retrieves a specific user by their ID.
    * @param id The ID of the user to retrieve.
    * @return ResponseEntity with UserPayload containing the requested user.
    */
   @GetMapping("/{id}")
   public ResponseEntity<UserPayload> getUserById(@PathVariable Long id) {
      return ResponseEntity.ok(userService.getUserById(id));
   }

   /**
    * Searches users based on the provided search criteria.
    * @param userSearch The search criteria.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with UserPayload containing the search results.
    */
   @GetMapping("/search")
   public ResponseEntity<UserPayload> searchUsers(UserSearch userSearch, @RequestParam(defaultValue = "0") int page){
      return ResponseEntity.ok(userService.searchUsers(userSearch, PageRequest.of(page, 10)));
   }

   /**
    * Updates an existing user.
    * @param request The payload containing updated user data.
    * @param principal The authenticated user.
    * @return ResponseEntity with UserPayload containing the updated user.
    */
   @PutMapping("/update")
   public ResponseEntity<UserPayload> updateUser(@RequestBody UserPayload request, Principal principal) {
      long input = Long.parseLong(principal.getName());
      if (userService.getUserById(input) != null)  {
         request.getDto().setId(input);
         return ResponseEntity.ok(userService.updateUser(request));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }

   /**
    * Deletes a user by their ID.
    * @param id The ID of the user to delete.
    * @param principal The authenticated user.
    * @return ResponseEntity with UserPayload containing the deleted user.
    */
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<UserPayload> deleteArticle(@PathVariable Long id, Principal principal) {
      long input = Long.parseLong(principal.getName());
      if (input == userService.getUserById(id).getDto().getId()) {
         return ResponseEntity.ok(userService.delete(id));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }
}
