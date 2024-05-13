package com.inkronsane.ReadArticlesServer.mapper;


import com.inkronsane.ReadArticlesServer.dto.ArticleDto;
import com.inkronsane.ReadArticlesServer.entity.*;
import java.util.*;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  uses = UserMapper.class)
/**
 * Mapping class
 * This class is used for more convenient mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public interface ArticleMapper {

   @Mapping(target = "id", ignore = true)
   @Mapping(source = "author", target = "author.username")
   @Mapping(source = "authorId", target = "author.id")
   @Mapping(source = "tags", target = "articleTags", qualifiedByName = "convertStringToTags")
   Article mapToArticle(ArticleDto articleDto);

   @Mapping(source = "author.username", target = "author")
   @Mapping(source = "author.id", target = "authorId")
   @Mapping(source = "articleTags", target = "tags", qualifiedByName = "convertTagsToString")
   ArticleDto mapToDto(Article article);
   /**
    * Mapping method from tag name to tag essence
    * This method takes a parameter and returns a result.
    * @param tags a list of strings with the name of the tags
    * @return a hash set of tags
    */
   @Named("convertStringToTags")
   default Set<Tag> convertStringToTags(Set<String> tags) {
      Set<Tag> tagSet = new HashSet<>();
      for (String tagName : tags) {
         Tag tag = new Tag();
         tag.setName(tagName);
         tagSet.add(tag);
      }
      return tagSet;
   }
   /**
    * Mapping method from tag name to tag essence
    * This method takes a parameter and returns a result.
    * @param tags list of tags
    * @return a hash set of tag names as a list of strings
    */
   @Named("convertTagsToString")
   default Set<String> convertTagsToString(Set<Tag> tags) {
      Set<String> tagSet = new HashSet<>();
      for (Tag tag : tags) {
         String tagName = tag.getName();
         tagSet.add(tagName);
      }
      return tagSet;
   }
}