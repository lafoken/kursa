package com.inkronsane.ReadArticlesServer.mapper;


import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  uses = {ArticleMapper.class, UserMapper.class})
/**
 * Mapping class
 * This class is used for more convenient mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public interface CommentMapper {

   @Mapping(target = "id", ignore = true)
   @Mapping(source = "articleId", target = "article.id")
   @Mapping(source = "authorId", target = "author.id")
   @Mapping(source = "authorUsername", target = "author.username")
   Comment mapToEntity(CommentDto commentDto);

   @Mapping(source = "article.id", target = "articleId")
   @Mapping(source = "author.id", target = "authorId")
   @Mapping(source = "author.username", target = "authorUsername")
   CommentDto mapToDto(Comment comment);
}