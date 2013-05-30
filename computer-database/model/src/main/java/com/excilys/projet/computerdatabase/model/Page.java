package com.excilys.projet.computerdatabase.model;

import java.util.List;

public class Page {

	private boolean first;
	private boolean last;
	private int pageNumber;
	private int total;
	private int displayFrom;
	private int displayTo;
	private List<Computer> computers;
	private final int maxAffichage;
	
	public Page(int page, int maxAffichage, int totalComputers, List<Computer> listeComputers) {
		
		this.maxAffichage = maxAffichage;
		total = totalComputers;
		
		pageNumber = page;
		
		//Liste des ordinateurs
		computers = listeComputers;
		
		computeDisplayFrom();	
		computeDisplayTo();	
		computeFirstPage();	
		computeLastPage();
	}
	
	private void computeLastPage(){
		//Derniere page
		if(total - (pageNumber+1)*maxAffichage >=1){
			last = false;
		} else {
			last = true;
		}
	}
	
	private void computeFirstPage(){
		//Première page
		if(pageNumber == 0) {
			first = true;
		} else {
			first = false;
		}
	}
	
	private void computeDisplayTo(){
		//DisplayTo
		if((total - (pageNumber+1)*maxAffichage)<1) {
			displayTo = total;
		} else {
			displayTo = (pageNumber +1)*maxAffichage;
		}
	}
	
	private void computeDisplayFrom(){
		//displayFrom
		displayFrom = total == 0 ? 0 : (pageNumber * maxAffichage +1);
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public int getTotal() {
		return total;
	}
	
	public List<Computer> getComputers() {
		return computers;
	}
	

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getDisplayTo(){
		return displayTo;
	}
	
	public int getDisplayFrom(){
		return displayFrom;
	}
	
	public boolean getLast(){
		return last;
	}
	
	public boolean getFirst(){
		return first;
	}
	
}
