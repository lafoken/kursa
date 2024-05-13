package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.entity.Role;
import com.inkronsane.ReadArticlesServer.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleServiceTest {

   @Autowired
   private RoleRepository roleRepository;

   @Test
   public void saveRole(){
      Role user = new Role(null, "USER");
      roleRepository.save(user);
   }
}
