package com.powers.multitenant.dao;

import java.io.Serializable; 
import java.util.List; 
 
/**
 * Generic DAO implmentation interface 
 * @author MuhammadSaif 
 * 
 * @param <T> 
 * @param <ID> 
 */ 
public interface GenericDAO<T> extends Serializable {   
 
    T findById(Long id, boolean lock);   
  
    T makePersistent(T entity);   
 
    void makeTransient(T entity);   
}  