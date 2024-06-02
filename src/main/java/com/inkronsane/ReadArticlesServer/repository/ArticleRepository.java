package com.inkronsane.ReadArticlesServer.repository;


import com.inkronsane.ReadArticlesServer.entity.Article;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
/**
 * Repository class
 * This class is used for more convenient work with the database
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>,
  JpaSpecificationExecutor<Article> {
}
