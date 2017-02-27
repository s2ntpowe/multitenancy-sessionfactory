package com.powers.multitenant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.powers.multitenant.dao.StudentDAO;
import com.powers.multitenant.model.Student;

@Service("studentService")
public class StudentServiceImpl implements StudentService {


	@Autowired
	private StudentDAO studentDAO;
	
	public void save(Student student) {
		studentDAO.saveStudent(student);
	}

	public boolean findByLogin(String userName, String password) {	
		Student stud =  studentDAO.findByUserName(userName);
		
		if(stud != null && stud.getPassword().equals(password)) {
			return true;
		} 
		
		return false;		
	}

	public boolean findByUserName(String userName) {
		Student stud = studentDAO.findByUserName(userName);
		
		if(stud != null) {
			return true;
		}
		
		return false;
	}

	public List<Student> getAllStudents() {
		List<Student> students = studentDAO.getStudents();
		return students;
	}

}
