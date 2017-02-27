package com.powers.multitenant.service;

import java.util.List;

import com.powers.multitenant.model.Student;

public interface StudentService {
	void save(Student student);
	boolean findByLogin(String userName, String password);
	boolean findByUserName(String userName);
	List<Student> getAllStudents();
}
