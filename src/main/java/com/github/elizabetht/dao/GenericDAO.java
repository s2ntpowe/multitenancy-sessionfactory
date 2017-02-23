package com.github.elizabetht.dao;

import java.io.Serializable; 
import java.util.List; 
 
/**
 * Generic DAO implmentation interface 
 * @author MuhammadSaif 
 * 
 * @param <T> 
 * @param <ID> 
 */ 
public interface GenericDAO<T> {   
 
    T findById(Long id, boolean lock);   
  
    T makePersistent(T entity);   
 
    void makeTransient(T entity);   
}  