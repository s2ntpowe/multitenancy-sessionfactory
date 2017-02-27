package com.powers.multitenant.dao;

import java.io.Serializable; 
import java.lang.reflect.ParameterizedType; 
import java.util.List;

import javax.annotation.Resource;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Default implementation. All hail ! 
 * @author msaif 
 * 
 * @param <T> 
 * @param <ID> 
 */ 

@Repository
public abstract class GenericHibernateDAO<T> implements Serializable, GenericDAO<T>{   

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<T> persistentClass;   
    
    @Autowired
    protected SessionFactory sessionFactory;
    


    @SuppressWarnings("unchecked") 
    public GenericHibernateDAO() { 
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];   
    }   
 
 
    protected Session getSession(){     	
        return sessionFactory.getCurrentSession();
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

    public Long save(T entity){      
    	System.out.println("Is transaction active " + TransactionSynchronizationManager.isActualTransactionActive());

   // 	sess.beginTransaction();
        Long id = (Long)getSession().save( entity ); 
  //    sess.getTransaction().commit();
  //      flush();

       return id;
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