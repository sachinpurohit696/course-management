package com.student.coursemanagement.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentsDto {
	
	private Long courseId;
	
	private Long assignmentId;
	
	private String title;
	
	private String description;
	
	private Timestamp dueDate;

	private Integer timeInMinutes;
	
	private String studentAnswers;
	
	private boolean submitted;
}
