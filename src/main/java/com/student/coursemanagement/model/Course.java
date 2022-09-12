package com.student.coursemanagement.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private Long courseId;
	
	@Column(name = "title", unique = true)
	private String title;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Sections> sections;
	
	@JsonManagedReference
	@OneToMany(mappedBy="course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Assignments> assignments;
	
	@JsonManagedReference
	@ManyToMany(mappedBy = "enrolledCourses", fetch = FetchType.LAZY)
    private Set<User> enrolledStudents = new HashSet<>();
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
}
