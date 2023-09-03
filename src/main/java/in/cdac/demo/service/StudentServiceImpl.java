package in.cdac.demo.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.cdac.demo.dao.CourseRepository;
import in.cdac.demo.dao.CredentialsRepository;
import in.cdac.demo.dao.StudentRepository;
import in.cdac.demo.entity.Course;
import in.cdac.demo.entity.Credentials;
import in.cdac.demo.entity.Student;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Override
	public List<Student> getStudentList(){
		
		List<Student> sd = (List<Student>) studentRepository.findAll();
		for (Student student : sd) {
			System.out.println(student.getEmail_id());
		}
		return (List<Student>)
				studentRepository.findAll();
	}

	@Override
	public Student saveRegisterDetails(Student student) {
		// TODO Auto-generated method stub
		
		// TODO: 1. Generate user ID[YYYYMMMCOURSE_NAMEStudent_Id] ex: 2023MARDAC204 an store in cred. table along with encryptrd password
		// TODO: 2. Auto enabling and disableing IDS based on course duration // chck while login-- if the course is expired dont login in s
		// TODO: 3 Integrateion with mailing system.
		
//		student.setStudent_id(getStudentID(student));
		
		Course selectedCourse = courseRepository.findById(student.getCourse_id()).get();
		Date startDate = selectedCourse.getStart_date();
		int year = startDate.getYear() +1900;
		String month = (new DateFormatSymbols().getShortMonths()[startDate.getMonth()])  ;
		
		String couserName = selectedCourse.getCourse_name();
		String tempPsd = generateSecurePassword();
//		student.setPassword(tempPsd);
		
		Student savedStudent = studentRepository.save(student);
		int StudentId = savedStudent.getStudent_id();

		String usrId = year+month.toUpperCase()+couserName+StudentId;
		System.out.println("UserID generated is : "+ usrId);
			
		Credentials credentials=new Credentials();
		credentials.setUser_id(usrId);
	    credentials.setPassword(tempPsd);
	    credentials.setStudent_id(StudentId);
		credentialsRepository.save(credentials);
	    
		sendEmail(student.getEmail_id(), usrId, tempPsd,"login");
		
		return savedStudent;
	}
	
	  public static String generateSecurePassword() {  
          
	        // generate a string of upper case letters having length 2  
	        String upperCaseStr = RandomStringUtils.random(2, 65, 90, true, true);  
	         
	        // generate a string of lower case letters having length 2  
	        String lowerCaseStr = RandomStringUtils.random(2, 97, 122, true, true);  
	          
	        // generate a string of numeric letters having length 2  
	        String numbersStr = RandomStringUtils.randomNumeric(2);  
	          
	        // generate a string of special chars having length 2  
	        String specialCharStr = RandomStringUtils.random(2, 33, 47, false, false);  
	          
	        // generate a string of alphanumeric letters having length 2  
	        String totalCharsStr = RandomStringUtils.randomAlphanumeric(2);  
	          
	        // concatenate all the strings into a single one  
	        String demoPassword = upperCaseStr.concat(lowerCaseStr)  
	          .concat(numbersStr)  
	          .concat(specialCharStr)  
	          .concat(totalCharsStr);  
	          
	        // create a list of Char that stores all the characters, numbers and special characters   
	        List<Character> listOfChar = demoPassword.chars()  
	                .mapToObj(data -> (char) data)  
	                .collect(Collectors.toList());  
	          
	        // use shuffle() method of the Collections to shuffle the list elements   
	        Collections.shuffle(listOfChar);  
	          
	        //generate a random string(secure password) by using list stream() method and collect() method  
	        String password = listOfChar.stream()  
	                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)  
	                .toString();  
	                  
	        // return RandomStringGenerator password to the main() method   
	        return password;  
	    }
	  
	  public boolean sendEmail(String toEmail,String userId, String password,String messageType) {
		 

	    	
	        final String senderEmail = "avishkarj977@gmail.com"; // Your Gmail address
	        final String senderPassword = "ctqejeucxxrgqemp";    // Your Gmail password
	        String recipientEmail = toEmail;//"venkatnarayana5678@gmail.com";   // Recipient's email address

	        Properties properties = new Properties();
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");

	        Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(senderEmail, senderPassword);
	            }
	        });

	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(senderEmail));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));	        	
	        	if(messageType.equalsIgnoreCase("login")) {
		            message.setSubject("UserID and Password for your Student Portal");
		            message.setText("Welcome to Student Portal! \nHere are your Login Credentails \n"
		            		+ "UserID :	 "+ userId +"\nPassword : 	"+password);	        		
	        	} else if(messageType.equalsIgnoreCase("forgotPassword")) {
		            message.setSubject("RESET PASSWORD for Student Portal");
		            message.setText("password re-generation for "+userId+" is successfull.\nHere is your new Password is "+password);
	        	}  


	          
	            Transport.send(message);
	            System.out.println("Email sent successfully.");
	            return true;
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            return false;
	        }
	    
	  }

	@Override
	public Credentials loginUser(Credentials credentials) {
		// TODO Auto-generated method stub
		//TODO check user details are theree or notin DB 
		Credentials selectedCredentailsField = new Credentials();
		try {
			selectedCredentailsField = credentialsRepository.findById(credentials.getUser_id()).get();
			//TODO : fetch student id from credentials
		  // using studentid fetch email id from student table
			// using email_id fetch the details from course table
			
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
		return selectedCredentailsField;
	}
	
	@Override
	public String courseValidation(int sid) {
		System.out.println(sid);
		Student studentDetails = studentRepository.findById(sid).get();
		System.out.println(studentDetails.toString());
		String courseId = studentDetails.getCourse_id();
		Course coursedetails= courseRepository.findById(courseId).get();
		Date endDate = coursedetails.getEnd_date();
		Date currentdate=new Date(System.currentTimeMillis());		 
		if (endDate.after(currentdate))
		{
		 return "Login successful";
		}
		else {
		 return "You cannot login as Course duration is expired! Please Register Again!";
		}
	}
	
	@Override
	public Boolean checkUserExists(String emailId) {
		
		
		List<Student> sd = (List<Student>) studentRepository.findAll();
		for (Student student : sd) {
			if(emailId.equalsIgnoreCase(student.getEmail_id())){
				System.out.println(student.getEmail_id());
				return true;
			}
		}
		return false;
				
	}
	
	


	@Override
	public String forgotPassword(String userID, int studentId) {
		Credentials cred = new Credentials();
		cred.setUser_id(userID);
		String genPass = generateSecurePassword();
		cred.setPassword(genPass);
		cred.setStudent_id(studentId);
		
		System.out.println("Generated password "+ genPass + "for user ID "+ userID);
		credentialsRepository.save(cred);
		
		Student sdgt = studentRepository.findById(studentId).get();
		String toEmail = sdgt.getEmail_id();
		
		sendEmail(toEmail,userID ,genPass,"forgotPassword");
		
		return "Password has reset successfully. please check your email";
	}
}
