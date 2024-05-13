package com.inkronsane.ReadArticlesServer.controller;


import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.service.TagService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing tags.
 * Author: Hybalo Oleksandr
 * Date: 2024-05-13
 */
@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "http://localhost:3000")
public class TagController {

   @Autowired
   private final TagService tagService;

   public TagController(TagService tagService) {
      this.tagService = tagService;
   }

   /**
    * Creates a new tag.
    * @param tagPayload The payload containing tag data.
    * @return ResponseEntity with TagPayload containing the created tag.
    */
   @PostMapping("/create")
   public ResponseEntity<TagPayload> createTag(@RequestBody TagPayload tagPayload) {
      return ResponseEntity.ok(tagService.create(tagPayload));
   }

   /**
    * Retrieves all tags.
    * @param page Page number for pagination (default is 0).
    * @return ResponseEntity with TagPayload containing the tags.
    */
   @GetMapping("/getAll")
   public ResponseEntity<TagPayload> getAllTags(@RequestParam(defaultValue = "0") int page) {
      return ResponseEntity.ok(tagService.getAll(PageRequest.of(page, 20)));
   }
}
