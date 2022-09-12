package com.student.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.coursemanagement.model.Assignments;

public interface AssignmentRepository extends JpaRepository<Assignments, Long>{

}
