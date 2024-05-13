package com.inkronsane.ReadArticlesServer.service;

import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.dto.search.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import com.inkronsane.ReadArticlesServer.exception.*;
import com.inkronsane.ReadArticlesServer.mapper.*;
import com.inkronsane.ReadArticlesServer.payload.*;
import com.inkronsane.ReadArticlesServer.repository.UserRepository;
import com.inkronsane.ReadArticlesServer.spec.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.*;

/**
 * Service for operations with users.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;
   private final PersonalInfoMapper personalInfoMapper;

   @Autowired
   private Validator userValidator;

   @Autowired
   private RoleService roleService;

   /**
    * Creates a new user based on the request.
    *
    * @param request an object that contains data for creating a user
    * @return an object that contains the response about creating a user
    */
   public UserPayload createUser(UserDto request) {
      UserPayload response = new UserPayload();
      try {
         User user = userMapper.mapToEntity(request);
         user.getRoles().clear();
         user.getRoles().add(roleService.getUserRole());
         userRepository.save(user);
         response.setInfo(200, "User created successfully");
      } catch (Exception e) {
         log.error("Error occurred for {} :", request.getUsername(), e.getMessage());
         response.setErrorInfo(500, "Failed to create user: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets all users from the database for the specified page.
    *
    * @param page an object representing the results page
    * @return an object that contains information about the received users
    */
   public UserPayload getAllUsers(PageRequest page) {
      UserPayload response = new UserPayload();
      try {
         List<UserDto> users =
           userRepository.findAll(page).getContent().stream()
             .map(userMapper::mapToDto)
             .collect(Collectors.toList());
         response.setInfo(200, "Users fetched successfully");
         response.setDtos(users);
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch users: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets the user by its ID.
    *
    * @param id the user ID
    * @return an object that contains information about the found user
    */
   @Cacheable(cacheNames = "users", key = "#id")
   public UserPayload getUserById(Long id) {
      UserPayload response = new UserPayload();
      try {
         Optional<User> userOptional = userRepository.findById(id);
         if (userOptional.isPresent()) {
            UserDto user = userMapper.mapToDto(userOptional.get());
            response.setInfo(200, "User fetched successfully");
            response.setDto(user);
         } else {
            response.setErrorInfo(404, "User not found");
            throw new UserNotFoundException(id);
         }
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to fetch user: " + e.getMessage());
      }
      return response;
   }

   /**
    * Updates user information.
    *
    * @param request an object containing new data for the user
    * @return an object that contains the user update response
    */
   @CacheEvict(cacheNames = "users", key = "#request.dto.id")
   @CachePut(cacheNames = "users", key = "#request.dto.id")
   public UserPayload updateUser(UserPayload request) {
      UserPayload response = new UserPayload();
      try {
         Optional<User> optionalUser =
           userRepository.findByUsername(request.getDto().getUsername());
         if (optionalUser.isPresent()) {
            Errors errors = new BeanPropertyBindingResult(request, "userDto");
            userValidator.validate(request, errors);
            if (errors.hasErrors()) {
               response.setErrorInfo(500, "Invalid user data");
               response.setErrors(errors.getAllErrors()); // List of errors
               return response;
            }
            User user = optionalUser.get();
            user.getRoles().clear();
            user.getRoles().add(roleService.getUserRole());
            user.setPersonalInfo(
              personalInfoMapper.mapToEntity(request.getDto().getPersonalInfo()));
            userRepository.save(user);
            response.setInfo(200, "User updated successfully");
         } else {
            response.setErrorInfo(404, "User not found");
         }
      } catch (Exception e) {
         log.error("Error occurred for {} :", request.getDto().getUsername(), e.getMessage());
         response.setErrorInfo(500, "Failed to update user: " + e.getMessage());
      }
      return response;
   }

   /**
    * Gets a user from the database by their name.
    *
    * @param username username
    * @return an object representing the user
    * @throws NoSuchElementException if the user with the specified name is not found
    */
   public User getTrueUserByUsername(String username) {
      return userRepository.findByUsername(username).orElseThrow();
   }

   /**
    * Gets the user from the database by their ID.
    *
    * @param id the user ID
    * @return an object representing the user
    * @throws NoSuchElementException if a user with the specified ID is not found
    */
   public User getTrueUserById(Long id) {
      return userRepository.findById(id).orElseThrow();
   }

   /**
    * Search for users according to the specified parameters.
    *
    * @param userSearch user search parameters
    * @param page result page
    * @return an object that contains information about found users
    */
   public UserPayload searchUsers(UserSearch userSearch, PageRequest page) {
      UserPayload response = new UserPayload();
      try {
         Specification<User> spec = new UserSpecification(userSearch);
         response.setDtos(userRepository.findAll(spec, page).stream()
           .map(userMapper::mapToDto)
           .collect(Collectors.toList()));
         response.setInfo(200, "Users found successfully");
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to search users: " + e.getMessage());
      }
      return response;
   }

   /**
    * Removes a user from the database by their ID.
    *
    * @param id the id of the user to delete
    * @return object that contains information about the result of the delete operation
    */
   @CacheEvict(cacheNames = "users", key = "#id")
   public UserPayload delete(Long id) {
      UserPayload response = new UserPayload();
      try {
         Optional<User> optionalComment = userRepository.findById(id);
         if (optionalComment.isPresent()) {
            userRepository.delete(optionalComment.get());
            response.setInfo(200, "User deleted successfully");
         } else {
            response.setErrorInfo(404, "User not found");
         }
      } catch (Exception e) {
         response.setErrorInfo(500, "Failed to delete user: " + e.getMessage());
      }
      return response;
   }

}