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
@Repository
@Transactional
public abstract class GenericHibernateDAO<T>   
implements GenericDAO<T>{   

    private Class<T> persistentClass;   
    private Session session;   
 
    @Autowired
    EntityManagerFactory entFactory;
    
    @SuppressWarnings("unchecked") 
    public GenericHibernateDAO() { 
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];   
    }   
 
    public void setSession(Session s) {   
        this.session = s; 
    }   
 
    protected Session getSession(){ 
        try{ 
        	ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
        HttpSession httpSession = request.getRequest().getSession();
                System.out.println( "getting current session [getCurrentSession()]" ); 
                SessionFactory sessionFactory = entFactory.unwrap(SessionFactory.class);
                System.out.println("HTTP SESSION AGENCY ATTRIBUTE: " + httpSession.getAttribute("agency"));
                String agency = "nga";
                if(httpSession.getAttribute("agency")!=null){
                	agency = httpSession.getAttribute("agency").toString();
                }
                this.session = sessionFactory
        		        .withOptions()
        		        .tenantIdentifier(agency)
        		        .openSession();

       
        } 
        catch (HibernateException e){ 
            System.out.println( "No Session is associated with current Thread. " + 
                    "Make sure that you are initiating DAO instances in the TransactionScope." + e ); 
        } 
        return this.session; 
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