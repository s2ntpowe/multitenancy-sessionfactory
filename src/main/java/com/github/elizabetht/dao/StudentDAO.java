package com.github.elizabetht.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.github.elizabetht.model.Student;

@Component
public class StudentDAO extends GenericHibernateDAO<Student> {

	@Autowired
	EntityManagerFactory entFactory;
	

	public List<Student> getStudents(){
		Criteria crit = createCriteria(Student.class);
		return crit.list();
	}
	public Student findByUserName(String username){
		Query query =  createQuery("from Student where userName = :userName")
								  .setString("userName",username);
		List<Student>students =  query.list();
		if(students!=null && students.size()>0){
			System.out.println("EMAIL: " + students.get(0).getEmailAddress());
			return students.get(0);
		}
		return null;
	}
	public Student saveStudent(Student stud){      
    	System.out.println("SAVE ENTITY....");
        Student s = save( stud ); 
        return s; 
    } 
}
