package com.powers.multitenant.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.powers.multitenant.model.Classroom;
import com.powers.multitenant.model.Student;

@Repository
public class ClassroomDAO extends GenericHibernateDAO<Student> implements Serializable {

	public List<Classroom> getAllClassrooms(){
		Criteria createCriteria = createCriteria(Classroom.class);
		return createCriteria.list();
	}
}
