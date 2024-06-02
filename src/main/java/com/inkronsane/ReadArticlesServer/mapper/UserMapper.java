package com.inkronsane.ReadArticlesServer.mapper;

import com.inkronsane.ReadArticlesServer.dto.UserDto;
import com.inkronsane.ReadArticlesServer.entity.*;
import java.util.*;
import org.mapstruct.*;
/**
 * Mapping class
 * This class is used for more convenient mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "articles", ignore = true)
   @Mapping(target = "comments", ignore = true)
   @Mapping(source = "role", target = "roles", qualifiedByName = "convertStringToRole")
   User mapToEntity(UserDto userDto);

   @Mapping(source = "roles", target = "role", qualifiedByName = "convertRoleToString") // Map Set<Role> to Role
   UserDto mapToDto(User user);
   /**
    * A mapping method from role name to role identity
    * This method takes a parameter and returns a result.
    * @param role role name
    * @return hash set of roles
    */
   @Named("convertStringToRole")
   default Set<Role> convertStringToRole(String role) {
      Set<Role> roleSet = new HashSet<>();
      Role newRole = new Role(null, role);
      roleSet.add(newRole);
      return roleSet;
   }
   /**
    * A mapping method from a hash set of roles to a role name
    * This method takes a parameter and returns a result.
    * @param roles hashset list with user roles
    * @return role name
    */
   @Named("convertRoleToString")
   default String convertRoleToString(Set<Role> roles) {
      String roleString = "";
      for (Role role : roles) {
         roleString = role.getName();
      }
      return roleString;
   }
}