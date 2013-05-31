package com.excilys.projet.computerdatabase.model;

import java.util.List;

public class PageEdition {

	private Computer computer;
	private List<Company> companies;
	
	
	public PageEdition(Computer computer, List<Company> companies) {
		this.computer = computer;
		this.companies = companies;
	}


	public Computer getComputer() {
		return computer;
	}


	public void setComputer(Computer computer) {
		this.computer = computer;
	}


	public List<Company> getCompanies() {
		return companies;
	}


	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	
	
}
