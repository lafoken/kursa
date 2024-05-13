package com.inkronsane.ReadArticlesServer.repository;


import com.inkronsane.ReadArticlesServer.entity.Article;
import com.inkronsane.ReadArticlesServer.entity.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
