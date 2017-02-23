package com.github.elizabetht.util;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
public class SessionAgencyHolder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String agency;
	
	public void setAgency(String agency){
		this.agency = agency;
	}
	public String getAgency(){
		return this.agency;
	}
}