package in.cdac.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cdac.demo.entity.Course;
import in.cdac.demo.entity.Credentials;
import in.cdac.demo.entity.Student;
import in.cdac.demo.service.StudentService;
import in.cdac.demo.service.StudentServiceImpl;

@RestController
@RequestMapping("/demo")
public class Controller {

	@Autowired 
	private StudentService studentService;
	
//	@GetMapping("/getName")
//	public String getName() {
//		
//		String password="123456";
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String hashedPassword= passwordEncoder.encode(password);
//		System.out.println(passwordEncoder.matches(password, hashedPassword));
//		return hashedPassword;
////		return studentService.getStudentList().toString();
//		
//		
//	}
	
	
	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4200/")
	public ResponseEntity<String> registeruser(@RequestBody Student student){
	try {
//		Course c = new Course();
//		student.setCourseId("PG-DAC");
//		/student.setCourse/(c);
//		System.out.println(student.getStudent_id());
//		System.out.println(student.getFull_name());
//		System.out.println(student.getEmail_id());
//		System.out.println(student.getContact_number());
		
		//following logic is used for creation of fresh record 
		if(!studentService.checkUserExists(student.getEmail_id())) {
//			System.out.println(student.toString());
			Student sDetail =  studentService.saveRegisterDetails(student);
			
			
			return ResponseEntity.ok("Student Details saved successfully with ID :"+sDetail.getStudent_id())	;			
		} else {
			return ResponseEntity.badRequest().body("Email Already Exists. You Cannot Register!");	
		}

		
	} catch (Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
		// TODO: handle exception
	}	
		
	}
	
	@PostMapping("/loginUser")
	@CrossOrigin(origins = "http://localhost:4200/")
	public ResponseEntity<String> loginUser(@RequestBody Credentials credentials){
	try {
//		Course c = new Course();
//		student.setCourseId("PG-DAC");
//		/student.setCourse/(c);
//		System.out.println(student.getStudent_id());
//		System.out.println(student.getFull_name());
//		System.out.println(student.getEmail_id());
//		System.out.println(student.getContact_number());
		
		Credentials sDetail =  studentService.loginUser(credentials);
		
		System.out.println(credentials.toString());
		if(sDetail.getStudent_id()!=0) {
			
			if(sDetail.getPassword().equals(credentials.getPassword())){
				String resp  =  studentService.courseValidation(sDetail.getStudent_id());
				return ResponseEntity.ok(resp);				
			}else {
				return ResponseEntity.badRequest().body("Password is Incorrect. Please Try Again");
			}

		}else {
			return ResponseEntity.badRequest().body("Login Unsuccessful");
		}
	} catch (Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
		// TODO: handle exception
	}	
	}
	
	@PostMapping("/forgotPassword")
	@CrossOrigin(origins = "http://localhost:4200/")
	public ResponseEntity<String> forgotPassword(@RequestBody Credentials credentials){
	try {
//		Course c = new Course();
//		student.setCourseId("PG-DAC");
//		/student.setCourse/(c);
//		System.out.println(student.getStudent_id());
//		System.out.println(student.getFull_name());
//		System.out.println(student.getEmail_id());
//		System.out.println(student.getContact_number());
		
		Credentials sDetail =  studentService.loginUser(credentials);
		
		System.out.println(credentials.toString());
		if(sDetail.getStudent_id()!=0) {
			String resp  =  studentService.forgotPassword(credentials.getUser_id(),sDetail.getStudent_id());
			return ResponseEntity.ok(resp);
		}else {
			return ResponseEntity.badRequest().body("User Doesn't exists. Please check teh user ID again.");
		}
	} catch (Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
		// TODO: handle exception
	}	
	}
	
	
}
