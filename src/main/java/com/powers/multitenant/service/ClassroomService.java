package com.powers.multitenant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.powers.multitenant.dao.ClassroomDAO;
import com.powers.multitenant.model.Classroom;

@Service("classroomService")
public class ClassroomService {

	@Autowired
	ClassroomDAO classroomDAO;
	
	public List<Classroom> getAllClassrooms()
	{
		return classroomDAO.getAllClassrooms();
	}
}
