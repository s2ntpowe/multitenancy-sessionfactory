package com.github.elizabetht.dao;

import java.io.Serializable; 
import java.lang.reflect.ParameterizedType; 
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria; 
import org.hibernate.HibernateException; 
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion; 
import org.hibernate.criterion.Example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Default implementation. All hail ! 
 * @author msaif 
 * 
 * @param <T> 
 * @param <ID> 
 */ 
@Transactional
@Repository
public abstract class GenericHibernateDAO<T>   
implements GenericDAO<T>{   

    private Class<T> persistentClass;   
    
    @Autowired
    protected SessionFactory sessionFactory;
    

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked") 
    public GenericHibernateDAO() { 
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];   
    }   
 
 
    protected Session getSession(){ 
    	Session session;
    	 try {
    	        session = sessionFactory.getCurrentSession();
    	    } catch (HibernateException e) {
    	        session = sessionFactory.openSession();
    	    }
        return session; 
    } 
 
    public Class<T> getPersistentClass() {   
        return persistentClass;   
    }   
 
    @SuppressWarnings({ "unchecked", "deprecation" })   
    public T findById(Long id, boolean lock) {   
        T entity;   
        if (lock)   
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);   
        else   
            entity = (T) getSession().load(getPersistentClass(), id);   
 
        return entity;   
    }   
 

    public T makePersistent(T entity) { 
        getSession().saveOrUpdate( entity ); 
        return entity;   
    }   
 
    public void makeTransient(T entity) {   
        getSession().delete(entity);   
    }   
 
    public void flush() {   
        getSession().flush();   
    }   
 
    public T save(T entity){      
    	System.out.println("SAVE ENTITY....");
    	Session sess = getSession();
    	sess.beginTransaction();
        sess.save( entity ); 
        sess.getTransaction().commit();
        return entity; 
    } 
 
    public void clear() {   
        getSession().clear();   
    }   
 
    protected Criteria createCriteria(Class persistentClass){
        return getSession().createCriteria(persistentClass);
    }
    protected Query createQuery(String queryString){
        return getSession().createQuery(queryString);
    }
    protected SQLQuery createSQLQuery(String queryString){
        return getSession().createSQLQuery(queryString);
    }
}  