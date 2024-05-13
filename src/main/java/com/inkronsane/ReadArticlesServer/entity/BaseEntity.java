package com.inkronsane.ReadArticlesServer.entity;


import java.io.*;
/**
 * Basic entity
 * This class is used to work with the database.
 * Author: Hybalo Oleksandr
 * Date: 2024|05|13
 */
public interface BaseEntity<T extends Serializable> {

   void setId(T id);

   T getId();
}