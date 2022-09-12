package com.student.coursemanagement.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.student.coursemanagement.dto.UserDto;
import com.student.coursemanagement.model.User;

public interface UserService {

	ResponseEntity<String> signUp(@Valid UserDto userDto);

	User getUserDetailsFromToken();

	ResponseEntity<String> updateUser(Long userId, @Valid UserDto userDto);

	ResponseEntity<String> deleteUser(Long userId);

	ResponseEntity<List<UserDto>> fetchUsers();

	ResponseEntity<List<UserDto>> fetchStudents();

}
