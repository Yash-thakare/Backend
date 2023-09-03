package in.cdac.demo.dao;

import org.springframework.data.repository.CrudRepository;

import in.cdac.demo.entity.Course;

public interface CourseRepository extends CrudRepository<Course, String>{

}
