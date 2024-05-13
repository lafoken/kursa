package com.inkronsane.ReadArticlesServer.dto;


import java.time.*;

public record PersonalInfoDto(
/**
 * DTO
 * This class is used for more convenient data mapping
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
  String firstName, String lastName, LocalDate birthDate, String info) {

}
