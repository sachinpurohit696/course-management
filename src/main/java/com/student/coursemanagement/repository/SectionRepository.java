package com.student.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.coursemanagement.model.Sections;

public interface SectionRepository extends JpaRepository<Sections, Long>{

}
