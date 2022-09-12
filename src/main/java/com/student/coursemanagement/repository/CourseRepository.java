package com.student.coursemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.coursemanagement.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long>{

	List<Course> findByCreatedBy(String userName);

}
