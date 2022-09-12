package com.student.coursemanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.coursemanagement.constants.CmsConstants;
import com.student.coursemanagement.dto.AuthRequestDto;
import com.student.coursemanagement.dto.AuthResponseDto;
import com.student.coursemanagement.dto.UserDetailsDto;
import com.student.coursemanagement.dto.UserDto;
import com.student.coursemanagement.jwt.JwtUtils;
import com.student.coursemanagement.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	/** Login API for instructor,student and admin */
	@PostMapping("/login")
	public ResponseEntity<Object> authenticateUser(@Valid @RequestBody AuthRequestDto authRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwtToken = CmsConstants.BEARER + jwtUtils.generateToken(authRequest.getUserName());
			UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return new ResponseEntity<>(new AuthResponseDto(jwtToken, userDetails.getUsername(), roles.get(0)),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(CmsConstants.INVALID_USERNAME_PASSWORD, HttpStatus.UNAUTHORIZED);
		}

	}

	/** Sign Up API for instructor,student and admin */
	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(@RequestBody UserDto userDto) {
		try {
			return userService.signUp(userDto);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(CmsConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
