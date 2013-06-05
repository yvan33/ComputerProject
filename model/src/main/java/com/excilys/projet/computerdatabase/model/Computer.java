package com.excilys.projet.computerdatabase.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="computer")
public class Computer {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="introduced")
	private Date introduced;
	@Column(name="discontinued")
	private Date discontinued;
	@ManyToOne(targetEntity=Company.class)
	private Company company;
	
	
	
	public Computer(int id, String name, Date introduced,
			Date discontinued, Company company) {
		
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}
	
	public Computer(int id, String name) {
		
		this.id = id;
		this.name = name;;
	}
	
	
	public Computer() {
		
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
	public Date getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}
	public Date getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
}
