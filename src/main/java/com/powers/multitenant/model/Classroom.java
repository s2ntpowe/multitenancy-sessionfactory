package com.powers.multitenant.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="classroom")
public class Classroom {
	@Id
	@Column(name = "class_id")
	@GeneratedValue
	private Long classId;
	
	@NotEmpty
	@Column(name = "name")
	private String classname;	

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom")
	private List<Student>students;
	
	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long id) {
		this.classId = id;
	}
	public List<Student> getStudents() {
		return students;
	}
	public String getName() {
		return classname;
	}

	public void setName(String cname) {
		this.classname = cname;
	}
}
