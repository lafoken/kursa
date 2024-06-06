package com.inkronsane.ReadArticlesServer.controller;


import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.service.CommentService;
import java.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing comments.
 * Author: Hybalo Oleksandr
 * Date: 2024-05-13
 */
@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

   @Autowired
   private final CommentService commentService;

   public CommentController(CommentService commentService) {
      this.commentService = commentService;
   }

   /**
    * Creates a new comment.
    * @param commentPayload The payload containing comment data.
    * @param principal The authenticated user.
    * @return ResponseEntity with CommentPayload containing the created comment.
    */
   @PostMapping("/create")
   public ResponseEntity<CommentPayload> createComment(@RequestBody CommentPayload commentPayload, Principal principal) {
      long input = Long.parseLong(principal.getName());
      commentPayload.getDto().setAuthorId(input);
      return ResponseEntity.ok(commentService.create(commentPayload));
   }

   /**
    * Searches comments based on the provided search criteria.
    * @param commentSearch The search criteria.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with CommentPayload containing the search results.
    */
   @GetMapping("/search")
   public ResponseEntity<CommentPayload> search(CommentSearch commentSearch, @RequestParam(defaultValue = "0") int page){
      return ResponseEntity.ok(commentService.searchComments(commentSearch, PageRequest.of(page, 10)));
   }

   /**
    * Updates an existing comment.
    * @param id The ID of the comment to update.
    * @param payload The payload containing updated comment data.
    * @param principal The authenticated user.
    * @return ResponseEntity with CommentPayload containing the updated comment.
    */
   @PatchMapping("/update/{id}")
   public ResponseEntity<CommentPayload> updateComment(@PathVariable Long id, @RequestBody CommentPayload payload, Principal principal){
      long input = Long.parseLong(principal.getName());
      if (input == commentService.getById(id).getDto().getAuthorId()) {
         return ResponseEntity.ok(commentService.update(payload, id));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }

   /**
    * Deletes a comment by its ID.
    * @param id The ID of the comment to delete.
    * @param principal The authenticated user.
    * @return ResponseEntity with CommentPayload containing the deleted comment.
    */
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<CommentPayload> delete(@PathVariable Long id, Principal principal) {
      long input = Long.parseLong(principal.getName());
      if (input == commentService.getById(id).getDto().getAuthorId()) {
         return ResponseEntity.ok(commentService.delete(id));
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }
}
