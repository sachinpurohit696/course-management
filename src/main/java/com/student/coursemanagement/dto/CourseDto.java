package com.student.coursemanagement.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

	private Long courseId;

	private String title;
	
	private String description;
	
	private List<SectionsDto> sections;
	
	private List<AssignmentsDto> assignments;
	
	private List<UserDto> enrolledStudents;
}
