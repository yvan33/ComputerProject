package com.excilys.projet.computerdatabase.service;

import com.excilys.projet.computerdatabase.model.*;

import java.util.List;



public interface GestionComputerService {

	Company getCompany(int id);

	void insertOrUpdate(Computer computer) ;

	List<Computer> getComputers(int debut, int nombre,SqlRequestOptions sqlRequestOptions) ;

	Integer getComputerCount(SqlRequestOptions sqlRequestOptions);

	void deleteComputer(int id);

	Computer getComputer(int id);

	List<Company> getCompanies();

	boolean isComputerExists(int id);

	Page createPage(int page, int maxAffichage,
                    SqlRequestOptions sqlRequestOptions);
	
	PageEdition createPageEdition(int idComputer);

}