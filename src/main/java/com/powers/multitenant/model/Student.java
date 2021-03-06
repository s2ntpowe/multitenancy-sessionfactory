package com.powers.multitenant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="student")
public class Student {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotEmpty
	@Size(min=4, max=20)
	@Column(name = "userName")
	private String userName;
	
	@NotEmpty
	@Column(name = "firstName")
	private String firstName;
	
	@NotEmpty
	@Column(name = "lastName")
	private String lastName;
	
	@NotEmpty
	@Size(min=4, max=8)
	@Column(name = "password")
	private String password;
	
	@NotEmpty
	@Email
	@Column(name = "emailAddress")
	private String emailAddress;
	
	@NotNull
	@Past
	@DateTimeFormat(pattern="MM/dd/yyyy")
	@Column(name = "dateOfBirth")
	private Date dateOfBirth;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Classroom classroom;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}	
}
