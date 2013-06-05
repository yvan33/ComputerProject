package com.excilys.projet.computerdatabase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="company")
public class Company {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="name")
	@OrderBy
	private String name;
	
	
	public Company() {
		
	}
	
	public Company(int id, String name) {
		
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
