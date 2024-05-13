package com.inkronsane.ReadArticlesServer.config;

import java.util.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Cache configuration class.
 * This class is used for convenient cache configuration.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class CacheConfig {

   @Bean
   public CacheManager cacheManager() {
      ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
      cacheManager.setCacheNames(Arrays.asList("VerificationCodes", "articles", "users", "comments", "tags"));
      return cacheManager;
   }
}