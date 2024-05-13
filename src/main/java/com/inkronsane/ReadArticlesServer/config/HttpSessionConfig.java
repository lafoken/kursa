package com.inkronsane.ReadArticlesServer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

@Configuration
@EnableRedisHttpSession
/**
 * Session configuration class.
 * This class is used for convenient configuration of http-sessions.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public class HttpSessionConfig {

   @Bean
   public HeaderHttpSessionIdResolver httpSessionIdResolver() {
      return HeaderHttpSessionIdResolver.xAuthToken();
   }
}