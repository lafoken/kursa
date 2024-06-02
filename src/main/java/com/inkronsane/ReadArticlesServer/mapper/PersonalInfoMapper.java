package com.inkronsane.ReadArticlesServer.mapper;


import com.inkronsane.ReadArticlesServer.dto.*;
import com.inkronsane.ReadArticlesServer.entity.*;
import org.mapstruct.Mapper;
/**
 * Mapping class
 * This class is used for more convenient mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
@Mapper(componentModel = "spring")
public interface PersonalInfoMapper {

   PersonalInfo mapToEntity(PersonalInfoDto personalInfo);

   PersonalInfoDto mapToDto(PersonalInfo personalInfo);
}