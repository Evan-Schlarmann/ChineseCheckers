// Java Program to Illustrate DepartmentRepository File

// Importing package module to this code
package com.chinesecheckers.serverside.repository;

// Importing required classes

import com.chinesecheckers.serverside.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository

// Interface
public interface UserRepository extends JpaRepository<User, Long> {
}