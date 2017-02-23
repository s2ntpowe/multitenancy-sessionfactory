package com.github.elizabetht.service;

import java.util.List;

import com.github.elizabetht.model.Student;

public interface StudentService {
	Student save(Student student);
	boolean findByLogin(String userName, String password);
	boolean findByUserName(String userName);
	List<Student> getAllStudents();
}
