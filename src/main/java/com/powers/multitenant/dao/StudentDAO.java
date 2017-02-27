package com.powers.multitenant.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.powers.multitenant.model.Student;

@Repository
public class StudentDAO extends GenericHibernateDAO<Student> implements Serializable {

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
	
	public Long saveStudent(Student stud){      
        Long id = save( stud );
        System.out.println("Saving student returns ID: " + id);        
        return id;
    }

	public Student makePersistent(Student entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeTransient(Student entity) {
		// TODO Auto-generated method stub
		
	} 
}
