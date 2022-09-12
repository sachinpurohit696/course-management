package com.student.coursemanagement.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AuthRequestDto {
	
	@NotBlank
	private String userName;

	@NotBlank
	private String password;

}
