package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.repository.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

/**
 * Service for operations with tags.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TagService {

   private final TagRepository tagRepository;

   /**
    * Creates a new tag based on the query.
    *
    * @param request object that contains the data to create the tag
    * @return an object that contains the response about creating a tag
    */
   public TagPayload create(TagPayload request) {
      TagPayload response = new TagPayload();
      try {
         Optional<Tag> existingTagOptional = tagRepository.findByName(request.getName());
         if (existingTagOptional.isPresent()) {
            response.setErrorInfo(400, "Tag already exists");
            return response;
         }
         Tag entity = new Tag();
         entity.setName(request.getName());
         tagRepository.save(entity);
         response.setInfo(200, "Tag created successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to create tag: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets all tags from the database for the specified page.
    *
    * @param page an object representing the results page
    * @return an object that contains information about received tags
    */
   public TagPayload getAll(PageRequest page) {
      TagPayload response = new TagPayload();
      try {
         response.setNames(
           tagRepository.findAll(page).getContent().stream()
             .map(Tag::getName)
             .toList());
         response.setInfo(200, "Tags fetched successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch tags: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets a tag by name.
    *
    * @param name tag name
    * @return an object containing information about the tag found, or an empty object if no tag is found
    */
   public Optional<Tag> getByName(String name) {
      return tagRepository.findByName(name);
   }
}