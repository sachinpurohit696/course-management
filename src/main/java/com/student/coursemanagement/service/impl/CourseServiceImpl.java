package com.student.coursemanagement.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.student.coursemanagement.dto.AssignmentsDto;
import com.student.coursemanagement.dto.CourseDto;
import com.student.coursemanagement.dto.SectionsDto;
import com.student.coursemanagement.dto.TopicsDto;
import com.student.coursemanagement.dto.UserDto;
import com.student.coursemanagement.enums.Roles;
import com.student.coursemanagement.model.Assignments;
import com.student.coursemanagement.model.Course;
import com.student.coursemanagement.model.Sections;
import com.student.coursemanagement.model.Topics;
import com.student.coursemanagement.model.User;
import com.student.coursemanagement.repository.AssignmentRepository;
import com.student.coursemanagement.repository.CourseRepository;
import com.student.coursemanagement.repository.SectionRepository;
import com.student.coursemanagement.repository.TopicsRepository;
import com.student.coursemanagement.repository.UserRepository;
import com.student.coursemanagement.service.CourseService;
import com.student.coursemanagement.service.UserService;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	TopicsRepository topicsRepository;
	
	@Autowired
	AssignmentRepository assignmentRepository;

	private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

	@Override
	public ResponseEntity<String> createCourse(CourseDto courseDto) {
		try {
			User user = userService.getUserDetailsFromToken();
			Course course = new Course();
			course.setTitle(courseDto.getTitle());
			course.setDescription(courseDto.getDescription());
			course.setCreatedBy(user != null ? user.getUserName() : null);
			course.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			course.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			List<Sections> sectionsList = createCourseSectionDetails(courseDto.getSections(), course);
			course.setSections(sectionsList);
			courseRepository.saveAndFlush(course);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("Course Added Successfully", HttpStatus.OK);
	}

	private List<Sections> createCourseSectionDetails(List<SectionsDto> sectionDto, Course course) {
		if (sectionDto != null && !sectionDto.isEmpty()) {
			List<Sections> sectionsList;
			Map<Long, Sections> sectionMap = new HashMap<>();
			if (CollectionUtils.isEmpty(course.getSections())) {
				sectionsList = new ArrayList<>();
			} else {
				sectionsList = course.getSections();
				sectionsList.forEach(s -> {
					sectionMap.put(s.getSectionId(), s);
				});
			}
			sectionDto.forEach(s -> {
				Sections section;
				if (sectionMap != null && sectionMap.size() > 0 && sectionMap.containsKey(s.getSectionId())) {
					section = sectionMap.get(s.getSectionId());
				} else {
					section = new Sections();
				}
				section.setCourse(course);
				section.setName(s.getName());
				section.setSectionNumber(s.getSectionNumber());
				List<Topics> topics = createTopicDetails(section, s.getTopics());
				section.setTopics(topics);
				sectionsList.add(section);
			});
			return sectionsList;
		}
		return null;
	}

	private List<Topics> createTopicDetails(Sections section, List<TopicsDto> topicsDtoList) {
		if (topicsDtoList != null && !topicsDtoList.isEmpty()) {
			List<Topics> topicsList;
			Map<Long, Topics> topicsMap = new HashMap<>();
			if (CollectionUtils.isEmpty(section.getTopics())) {
				topicsList = new ArrayList<>();
			} else {
				topicsList = section.getTopics();
				topicsList.forEach(s -> {
					topicsMap.put(s.getTopicId(), s);
				});
			}

			topicsDtoList.forEach(t -> {
				Topics topic;
				if (topicsMap != null && topicsMap.size() > 0 && topicsMap.containsKey(t.getTopicId())) {
					topic = topicsMap.get(t.getTopicId());
				} else {
					topic = new Topics();
				}
				topic.setName(t.getName());
				topic.setContent(t.getContent());
				topic.setSections(section);
				topic.setReferenceUrl(t.getReferenceUrl());
				topicsList.add(topic);
			});
			return topicsList;
		}
		return null;
	}

	@Override
	public ResponseEntity<String> updateCourse(CourseDto courseDto, Long courseId) {
		try {
			User user = userService.getUserDetailsFromToken();
			Optional<Course> courseOptional = courseRepository.findById(courseId);
			if (!courseOptional.isPresent()) {
				return new ResponseEntity<String>("Course Details Not Found with given course id",
						HttpStatus.BAD_REQUEST);
			}
			Course course = courseOptional.get();
			course.setTitle(courseDto.getTitle());
			course.setDescription(courseDto.getDescription());
			course.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			List<Sections> sectionsList = createCourseSectionDetails(courseDto.getSections(), course);
			course.setSections(sectionsList);
			courseRepository.saveAndFlush(course);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("Course Details Updated Successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<CourseDto>> fetchAllCourses() {
		User user = userService.getUserDetailsFromToken();
		List<Course> courseList;
		if (Roles.ADMIN.name().equals(user.getRole()) || Roles.STUDENT.name().equals(user.getRole())) {
			courseList = courseRepository.findAll();
		} else {
			courseList = courseRepository.findByCreatedBy(user.getUserName());
		}
		List<CourseDto> courseDtoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(courseList)) {
			courseList.forEach(c -> {
				CourseDto courseDto = new CourseDto();
				courseDto.setCourseId(c.getCourseId());
				courseDto.setDescription(c.getDescription());
				courseDto.setTitle(c.getTitle());

				// set section details
				if (!CollectionUtils.isEmpty(c.getSections())) {
					List<SectionsDto> sectionsDtoList = new ArrayList<>();
					c.getSections().forEach(s -> {
						SectionsDto sectionsDto = new SectionsDto();
						sectionsDto.setSectionId(s.getSectionId());
						sectionsDto.setName(s.getName());
						sectionsDto.setSectionNumber(s.getSectionNumber());

						// set topic details section wise
						if (!CollectionUtils.isEmpty(s.getTopics())) {
							List<TopicsDto> topicsDtoList = new ArrayList<>();
							s.getTopics().forEach(t -> {
								TopicsDto topicsDto = new TopicsDto();
								topicsDto.setContent(t.getContent());
								topicsDto.setTopicId(t.getTopicId());
								topicsDto.setReferenceUrl(t.getReferenceUrl());
								topicsDto.setName(t.getName());
								topicsDtoList.add(topicsDto);
							});
							sectionsDto.setTopics(topicsDtoList);
						}
						sectionsDtoList.add(sectionsDto);
					});
					courseDto.setSections(sectionsDtoList);
					// set assignment details
					if (!CollectionUtils.isEmpty(c.getAssignments())) {
						List<AssignmentsDto> assignmentsDtoList = new ArrayList<>();
						c.getAssignments().forEach(a -> {
							AssignmentsDto assignmentsDto = new AssignmentsDto();
							assignmentsDto.setAssignmentId(a.getAssignmentId());
							assignmentsDto.setDescription(a.getDescription());
							assignmentsDto.setDueDate(a.getDueDate());
							assignmentsDto.setTitle(a.getTitle());
							assignmentsDtoList.add(assignmentsDto);
						});
						courseDto.setAssignments(assignmentsDtoList);
					}
					courseDtoList.add(courseDto);
				}
			});
		}

		return new ResponseEntity<List<CourseDto>>(courseDtoList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addAssignment(@Valid AssignmentsDto assignmentsDto) {
		Optional<Course> courseOptional = courseRepository.findById(assignmentsDto.getCourseId());
		if (!courseOptional.isPresent()) {
			return new ResponseEntity<String>("Course Details Not Found with given course id", HttpStatus.BAD_REQUEST);
		}
		Course course = courseOptional.get();
		Assignments assignments = new Assignments();
		assignments.setCourse(course);
		assignments.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		assignments.setDescription(assignmentsDto.getDescription());
		assignments.setTitle(assignmentsDto.getTitle());
		assignments.setDueDate(assignmentsDto.getDueDate());
		assignments.setSubmitted(false);
		assignments.setTimeInMinutes(assignments.getTimeInMinutes());
		course.getAssignments().add(assignments);
		courseRepository.saveAndFlush(course);
		return new ResponseEntity<String>("Assignment added successfully", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<CourseDto> fetchEnrolledStudents(Long courseId) {
		CourseDto courseDto = new CourseDto();
		List<UserDto> enrolledStudentsDto = new ArrayList<>();
		Optional<Course> courseOptional = courseRepository.findById(courseId);
		if (courseOptional.isPresent()) {
			courseDto.setTitle(courseOptional.get().getTitle());
			Set<User> enrolledUsersList = courseOptional.get().getEnrolledStudents();
			if (!CollectionUtils.isEmpty(enrolledUsersList)) {
				enrolledUsersList.forEach(u -> {
					UserDto dto = new UserDto();
					dto.setContactNumber(u.getContactNumber());
					dto.setName(u.getName());
					dto.setRole(u.getRole());
					dto.setUserName(u.getUserName());
					enrolledStudentsDto.add(dto);
				});
				courseDto.setEnrolledStudents(enrolledStudentsDto);
			}

		}
		return new ResponseEntity<>(courseDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> enrollCourse(Long courseId) {
		User user = userService.getUserDetailsFromToken();
		Optional<Course> courseOptional = courseRepository.findById(courseId);
		if (!courseOptional.isPresent()) {
			return new ResponseEntity<String>("Course Details Not Found with given course id", HttpStatus.BAD_REQUEST);
		}
		Course course = courseOptional.get();
		user.getEnrolledCourses().add(course);
		course.getEnrolledStudents().add(user);
		userRepository.saveAndFlush(user);
		courseRepository.saveAndFlush(course);
		return new ResponseEntity<String>("You have been successfully enrolled for this course", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> submitAssignment(Long courseId, @Valid AssignmentsDto assignmentsDto) {
		User user = userService.getUserDetailsFromToken();
		Optional<Course> courseOptional = courseRepository.findById(courseId);
		if (!courseOptional.isPresent()) {
			return new ResponseEntity<String>("Course Details Not Found with given course id", HttpStatus.BAD_REQUEST);
		}
		Course course = courseOptional.get();
		if (!CollectionUtils.isEmpty(course.getAssignments())) {
			Assignments assignmentDetails = course.getAssignments().stream()
					.filter(assignment -> assignment.getAssignmentId().equals(assignmentsDto.getAssignmentId()))
					.findFirst().get();
			assignmentDetails.setStudentAnswers(assignmentsDto.getStudentAnswers());
			assignmentRepository.saveAndFlush(assignmentDetails);
		}
		return new ResponseEntity<String>("Assignment Submitted Successfully", HttpStatus.OK);
	}

}
