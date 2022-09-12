package com.student.coursemanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.coursemanagement.dto.UserDto;
import com.student.coursemanagement.enums.Roles;
import com.student.coursemanagement.model.User;
import com.student.coursemanagement.repository.UserRepository;
import com.student.coursemanagement.service.UserService;

@Service
public class UsersServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

	@Override
	public ResponseEntity<String> signUp(UserDto userDto) {
		String role = userDto.getRole().toUpperCase();
		if (Roles.ADMIN.name().equals(role) || Roles.INSTRUCTOR.name().equals(role)
				|| Roles.STUDENT.name().equals(role)) {
			Optional<User> existingUser = userRepository.findByUserName(userDto.getUserName());
			if (existingUser.isPresent()) {
				return new ResponseEntity<String>("User Already Exists with given userName : " + userDto.getUserName(),
						HttpStatus.CONFLICT);
			}
			User user = new User();
			user.setName(userDto.getName());
			user.setContactNumber(userDto.getContactNumber());
			user.setPassword(encoder.encode(userDto.getPassword()));
			user.setRole(role);
			user.setUserName(userDto.getUserName());
			userRepository.saveAndFlush(user);
			return new ResponseEntity<String>("User Created Successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Given Role is not acceptable", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public User getUserDetailsFromToken() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> user = userRepository.findByUserName(userDetails.getUsername());
		return user.isPresent() ? user.get() : null;
	}

	@Override
	public ResponseEntity<String> updateUser(Long userId, @Valid UserDto userDto) {
		Optional<User> existingUser = userRepository.findById(userId);
		if (existingUser.isPresent()) {
			User user = existingUser.get();
			user.setName(userDto.getName());
			user.setContactNumber(userDto.getContactNumber());
			user.setRole(userDto.getRole());
			userRepository.saveAndFlush(user);
			return new ResponseEntity<String>("User Details Update Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<String>("User Details Not Found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> deleteUser(Long userId) {
		Optional<User> existingUser = userRepository.findById(userId);
		if (existingUser.isPresent()) {
			userRepository.deleteById(userId);
			userRepository.flush();
			return new ResponseEntity<String>("User Deleted Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<String>("User Details Not Found", HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<List<UserDto>> fetchUsers() {
		List<User> users = userRepository.findAll();
		List<UserDto> dtoList = new ArrayList<UserDto>();
		if (users != null && !users.isEmpty()) {
			users.forEach(user -> {
				UserDto dto = new UserDto();
				dto.setContactNumber(user.getContactNumber());
				dto.setUserId(user.getUserId());
				dto.setName(user.getName());
				dto.setRole(user.getRole());
				dto.setUserName(user.getRole());
				dtoList.add(dto);
			});
		}
		return new ResponseEntity<List<UserDto>>(dtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<UserDto>> fetchStudents() {
		List<User> users = userRepository.findByRole(Roles.STUDENT.name());
		List<UserDto> dtoList = new ArrayList<UserDto>();
		if (users != null && !users.isEmpty()) {
			users.forEach(user -> {
				UserDto dto = new UserDto();
				dto.setContactNumber(user.getContactNumber());
				dto.setUserId(user.getUserId());
				dto.setName(user.getName());
				dto.setRole(user.getRole());
				dto.setUserName(user.getRole());
				dtoList.add(dto);
			});
		}
		return new ResponseEntity<List<UserDto>>(dtoList, HttpStatus.OK);
	}

}
