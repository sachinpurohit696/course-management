package com.student.coursemanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.coursemanagement.dto.AssignmentsDto;
import com.student.coursemanagement.dto.CourseDto;
import com.student.coursemanagement.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	CourseService courseService;

	/** Fetch all courses for student and admin. Instructor can view only their courses */
	@GetMapping("/fetchAll")
	@PreAuthorize("hasAuthority('INSTRUCTOR') OR hasAuthority('ADMIN') OR hasAuthority('STUDENT')")
	public ResponseEntity<List<CourseDto>> fetchAllCourses() {
		return courseService.fetchAllCourses();
	}

	/** Create Course API for Instructors */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('INSTRUCTOR')")
	public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDto courseDto) {
		return courseService.createCourse(courseDto);
	}

	/** Update Course API for Instructors */
	@PutMapping("/update/{courseId}")
	@PreAuthorize("hasAuthority('INSTRUCTOR')")
	public ResponseEntity<String> updateCourse(@PathVariable("courseId") Long courseId,
			@Valid @RequestBody CourseDto courseDto) {
		return courseService.updateCourse(courseDto, courseId);
	}

	/** Add Assignment API for instructors to add assignment to particular Course*/
	@PostMapping("/addAssignment")
	@PreAuthorize("hasAuthority('INSTRUCTOR')")
	public ResponseEntity<String> addAssignment(@Valid @RequestBody AssignmentsDto assignmentsDto) {
		return courseService.addAssignment(assignmentsDto);
	}

	/** Add Assignment API for instructors to add assignment to particular Course*/
	@GetMapping("/enrolledStudents/{courseId}")
	@PreAuthorize("hasAuthority('INSTRUCTOR') OR hasAuthority('ADMIN')")
	public ResponseEntity<CourseDto> fetchEnrolledStudents(@PathVariable("courseId") Long courseId) {
		return courseService.fetchEnrolledStudents(courseId);
	}

	/** Enroll course api for students*/
	@PostMapping("/{courseId}/enroll")
	@PreAuthorize("hasAuthority('STUDENT')")
	public ResponseEntity<String> enrollCourse(@PathVariable("courseId") Long courseId) {
		return courseService.enrollCourse(courseId);
	}

	/** Submit  course  assignment api for students*/
	@PutMapping("/{courseId}/submitAssignment")
	@PreAuthorize("hasAuthority('STUDENT')")
	public ResponseEntity<String> submitAssignment(@PathVariable("courseId") Long courseId,
			@Valid @RequestBody AssignmentsDto assignmentsDto) {
		return courseService.submitAssignment(courseId, assignmentsDto);
	}

}
