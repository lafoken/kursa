package com.inkronsane.ReadArticlesServer.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.inkronsane.ReadArticlesServer.dto.ArticleDto;
import com.inkronsane.ReadArticlesServer.entity.Article;
import com.inkronsane.ReadArticlesServer.entity.Tag;
import com.inkronsane.ReadArticlesServer.mapper.ArticleMapper;
import com.inkronsane.ReadArticlesServer.payload.ArticlePayload;
import com.inkronsane.ReadArticlesServer.repository.ArticleRepository;
import com.inkronsane.ReadArticlesServer.service.ArticleService;
import com.inkronsane.ReadArticlesServer.service.TagService;
import com.inkronsane.ReadArticlesServer.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

   @Mock
   private ArticleRepository articleRepository;

   @Mock
   private UserService userService;

   @Mock
   private TagService tagService;

   @Mock
   private ArticleMapper articleMapper;

   @InjectMocks
   private ArticleService articleService;

   @Test
   void create() {
      // Mocking TagService to return a Tag
      Tag mockTag = new Tag(); // create a mock Tag object
      when(tagService.getByName("NOT_CAT")).thenReturn(Optional.of(mockTag)); // mock the behavior

      // Creating Article
      Set<Tag> tags = new HashSet<>();
      tags.add(tagService.getByName("NOT_CAT").orElseThrow());
      Article article = Article.builder()
        .id(null)
        .title("My work killing me.My work killing me.")
        .content("SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.SAD.")
        .author(userService.getTrueUserById(1L))
        .articleTags(tags)
        .build();
      ArticleDto articleDto = articleMapper.mapToDto(article);
      ArticlePayload articlePayload = new ArticlePayload();
      articlePayload.setDto(articleDto);

      // Mocking ArticleRepository to return the saved Article
      when(articleRepository.save(Mockito.any(Article.class))).thenReturn(article);

      // Calling the method under test
      ArticlePayload response = articleService.create(articlePayload);

      // Asserting the response
      assertThat(response.getStatusCode()).isEqualTo(200);
   }

   // Other test methods here
}
