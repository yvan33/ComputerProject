package com.excilys.projet.computerdatabase.dao;

import com.excilys.projet.computerdatabase.model.Computer;
import com.excilys.projet.computerdatabase.model.SqlRequestOptions;

import java.util.List;



public interface GestionComputerDao {

	List<Computer> getComputers(int debut, int nombre,
                                SqlRequestOptions sqlRequestOptions);

	Computer getComputer(int id);

	Integer getComputerCount(SqlRequestOptions sqlRequestOptions);

	boolean deleteComputer(int id);

	boolean updateComputer(Computer computer);

	boolean insertComputer(Computer computer);

	boolean isComputerExists(int id);

}