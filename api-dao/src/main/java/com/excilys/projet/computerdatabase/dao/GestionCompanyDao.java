package com.excilys.projet.computerdatabase.dao;

import java.util.List;

import com.excilys.projet.computerdatabase.model.Company;

public interface GestionCompanyDao {

	List<Company> getCompanies();

	Company getCompany(int id);

}