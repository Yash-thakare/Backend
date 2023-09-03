package in.cdac.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.cdac.demo.entity.Credentials;
import in.cdac.demo.entity.Student;

@Service
public interface StudentService {

	public List<Student> getStudentList();
	
	public Student saveRegisterDetails(Student student);

	public Credentials loginUser(Credentials credentials);

	public String courseValidation(int sid);

	public Boolean checkUserExists(String emailId);

	public String forgotPassword(String userID, int studentId);
}
