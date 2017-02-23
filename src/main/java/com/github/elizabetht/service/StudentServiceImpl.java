package com.github.elizabetht.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.elizabetht.dao.StudentDAO;
import com.github.elizabetht.model.Student;


@Service("studentService")
public class StudentServiceImpl implements StudentService {


	@Autowired
	private StudentDAO studentDAO;
	@Transactional
	public Student save(Student student) {
		return studentDAO.saveStudent(student);
	}
	public boolean findByLogin(String userName, String password) {	
		Student stud =  studentDAO.findByUserName(userName);
		
		if(stud != null && stud.getPassword().equals(password)) {
			return true;
		} 
		
		return false;		
	}
	@Transactional
	public boolean findByUserName(String userName) {
		Student stud = studentDAO.findByUserName(userName);
		
		if(stud != null) {
			return true;
		}
		
		return false;
	}
	@Transactional
	public List<Student> getAllStudents() {
		List<Student> students = studentDAO.getStudents();
		return students;
	}

}
