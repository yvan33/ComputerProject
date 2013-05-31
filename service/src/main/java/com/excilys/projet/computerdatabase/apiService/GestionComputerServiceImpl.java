package com.excilys.projet.computerdatabase.apiService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.projet.computerdatabase.dao.GestionCompanyDao;
import com.excilys.projet.computerdatabase.dao.GestionComputerDao;
import com.excilys.projet.computerdatabase.model.*;
import com.excilys.projet.computerdatabase.service.GestionComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Component
@Transactional(readOnly=true)
@Scope("singleton")
public class GestionComputerServiceImpl implements GestionComputerService {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GestionComputerServiceImpl.class);
	
	@Autowired
	private GestionComputerDao computerDao;
	@Autowired
	private GestionCompanyDao companyDao;
	@Autowired
	private static GestionComputerService gestionComputerService;
	
	private GestionComputerServiceImpl() {
	}
	
	@Override
	public Company getCompany(int id) throws SQLException{
		
		Company c = null;
			Assert.isTrue(computerDao.isComputerExists(id),"l'id de l'ordinateur n'existe pas.");
			c = companyDao.getCompany(id);

		return c;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void insertOrUpdate(Computer computer) throws SQLException{

			if(computer.getId()!=0){
				computerDao.updateComputer(computer);
			} else {
				computerDao.insertComputer(computer);
			}
	}
	
	@Override
	public List<Computer> getComputers(int debut, int nombre, SqlRequestOptions sqlRequestOptions) throws SQLException{
		List<Computer> computers = null;
		try{
			computers = computerDao.getComputers(debut, nombre, sqlRequestOptions);
		} catch (SQLException e){
			logger.warn("Erreur lors de la récupération de la liste des ordinateurs" + e.getMessage());
			throw e;
		}
		return computers;
	}

	@Override
	public Integer getComputerCount(SqlRequestOptions sqlRequestOptions) throws SQLException{
		Integer i = null;
		try {
			i = computerDao.getComputerCount(sqlRequestOptions);
		} catch(SQLException e){
			logger.warn("Erreur lors de la récupération du compte des ordinateurs" + e.getMessage());
			throw e;
		}
		
		return i;
	}
	
	@Override
	@Transactional(readOnly=false)
	public void updateComputer(Computer c) throws SQLException{
		try {
			computerDao.updateComputer(c);
		} catch(SQLException e) {
			logger.warn("Erreur lors de l'update d'un ordinateur" + e.getMessage());		
		}
	}
	
	@Override
	@Transactional(readOnly=false)
	public void deleteComputer(int id) throws SQLException{
		try{
			if(computerDao.isComputerExists(id)) {
				computerDao.deleteComputer(id);
			} else {
				throw new IllegalArgumentException("L'id de l'ordinateur n'existe pas.");
			}

		} catch(SQLException e){
			logger.warn("Erreur lors de la suppression d'un ordinateur" + e.getMessage());
		}
	}
	
	@Override
	public Computer getComputer(int id) throws SQLException{
		Computer c = null;
		try {
			c = computerDao.getComputer(id);
		} catch (SQLException e) {
			logger.warn("Erreur lors de la récupération d'un ordinateur" + e.getMessage());
			throw e;
		}
		
		return c;
	}
	
	@Override
	public List<Company> getCompanies() throws SQLException{
		List<Company> companies = null;
		try {
			companies = companyDao.getCompanies();
		} catch (SQLException e){
			logger.warn("Erreur lors de la récupération de la liste des sociétés" + e.getMessage());
			throw e;
		}
		return companies;
	}
	
	@Override
	public boolean isComputerExists(int id) throws SQLException {		
		boolean b = false;
		try {
			b = computerDao.isComputerExists(id);
		} catch(SQLException e){
			logger.warn("Erreur lors de la récupération d'une société" + e.getMessage());
			throw e;
		}
		return b;
	}

	@Override
	public Page createPage(int page, int maxAffichage, SqlRequestOptions sqlRequestOptions) throws SQLException{
		int total = 0;
		List<Computer> computers = new ArrayList<Computer>();
		
		try {
			computers = computerDao.getComputers(page*maxAffichage, maxAffichage, sqlRequestOptions);
			total = computerDao.getComputerCount(sqlRequestOptions);
		} catch(SQLException e){
			logger.warn("Erreur lors de la création d'une page" + e.getMessage());
			throw e;
		}
		if(page<0 || (page>0 && (total - page*maxAffichage<0))){
			page=0;
		}
		return new Page(page, maxAffichage, total,
				computers);
	}

	@Override
	public PageEdition createPageEdition(int idComputer) throws SQLException {
		Computer computer = null;
		List<Company> companies = new ArrayList<Company>();
		
		try {
			if(computerDao.isComputerExists(idComputer)){
				computer = computerDao.getComputer(idComputer);
				companies = companyDao.getCompanies();
			} else {
				throw new IllegalArgumentException("l'id de l'ordinateur n'existe pas.");
			}
			
		} catch(SQLException e){
			logger.warn("Erreur lors de la création de la page d'édition " + e.getMessage());
			throw e;
		}
		return new PageEdition(computer, companies);
	}
	
}