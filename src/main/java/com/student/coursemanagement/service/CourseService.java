package com.student.coursemanagement.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.student.coursemanagement.dto.AssignmentsDto;
import com.student.coursemanagement.dto.CourseDto;

public interface CourseService {

	ResponseEntity<String> createCourse(CourseDto courseDto);

	ResponseEntity<String> updateCourse(CourseDto courseDto, Long courseId);

	ResponseEntity<List<CourseDto>> fetchAllCourses();

	ResponseEntity<String> addAssignment(@Valid AssignmentsDto assignmentsDto);

	ResponseEntity<CourseDto> fetchEnrolledStudents(Long courseId);

	ResponseEntity<String> enrollCourse(Long courseId);

	ResponseEntity<String> submitAssignment(Long courseId, @Valid AssignmentsDto assignmentsDto);

}
