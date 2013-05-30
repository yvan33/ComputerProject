package com.excilys.projet.computerdatabase.dao;

import java.sql.SQLException;
import java.util.List;

import com.excilys.projet.computerdatabase.model.Company;

public interface GestionCompanyDao {

	List<Company> getCompanies() throws SQLException;

	Company getCompany(int id) throws SQLException;

}