package com.student.coursemanagement.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionsDto {

	private Long sectionId;

	private String name;

	private Integer sectionNumber;
	
	private List<TopicsDto> topics;

}
