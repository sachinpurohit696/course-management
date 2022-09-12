package com.student.coursemanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.coursemanagement.dto.UserDto;
import com.student.coursemanagement.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	/** Fetch All users i.e. Instructors, Students API for Admin*/
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<UserDto>> fetchUsers() {
		return userService.fetchUsers();
	}
	
	/** Update Instructor,Student API for Admin*/
	@PutMapping("{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> updateUser(@PathVariable(name = "userId") Long userId, @Valid @RequestBody UserDto userDto) {
		return userService.updateUser(userId, userDto);
	}
	
	/** Delete Instructor,Student API for Admin*/
	@DeleteMapping("{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long userId) {
		return userService.deleteUser(userId);
	}
	
	/** Fetch All Students API for Admin*/
	@GetMapping("/fetchStudents")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<UserDto>> fetchStudents() {
		return userService.fetchStudents();
	}

}
