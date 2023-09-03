package in.cdac.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import in.cdac.demo.entity.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student,Integer> {

	
}
