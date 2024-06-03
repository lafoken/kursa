package com.inkronsane.ReadArticlesServer.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Override
   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
        .allowedOrigins("https://inkronsane.github.io")
        .allowedMethods("GET", "POST", "PUT", "DELETE");
   }
}
