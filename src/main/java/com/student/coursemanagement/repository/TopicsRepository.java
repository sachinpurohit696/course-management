package com.student.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.coursemanagement.model.Topics;

public interface TopicsRepository extends JpaRepository<Topics, Long> {

}
