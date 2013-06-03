package com.excilys.projet.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.projet.computerdatabase.dao.GestionCompanyDao;
import com.excilys.projet.computerdatabase.dao.GestionComputerDao;
import com.excilys.projet.computerdatabase.model.*;
import com.excilys.projet.computerdatabase.service.GestionComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@Transactional(readOnly = true)
public class GestionComputerServiceImpl implements GestionComputerService {


	private static final Logger logger = LoggerFactory
			.getLogger(GestionComputerServiceImpl.class);

	@Autowired
	private GestionComputerDao computerDao;
	@Autowired
	private GestionCompanyDao companyDao;
	@Autowired
	private static GestionComputerService gestionComputerService;

	@Override
	public Company getCompany(int id) {

		Company c = null;
		Assert.isTrue(computerDao.isComputerExists(id),
				"l'id de l'ordinateur n'existe pas.");
		c = companyDao.getCompany(id);
		return c;
	}

	@Override
	@Transactional(readOnly = false)
	public void insertOrUpdate(Computer computer) {
		boolean res;
		if (computer.getId() != 0) {
			res = computerDao.updateComputer(computer);
		} else {
			res = computerDao.insertComputer(computer);
		}
		Assert.isTrue(res, "l'id de l'ordinateur n'existe pas.");
	}

	@Override
	public List<Computer> getComputers(int debut, int nombre,
			SqlRequestOptions sqlRequestOptions) {
		List<Computer> computers = null;

		computers = computerDao.getComputers(debut, nombre, sqlRequestOptions);
		Assert.notNull(computers,
				"Erreur lors de la récupération des ordinateurs.");
		// logger.warn("Erreur lors de la récupération de la liste des ordinateurs"
		// + e.getMessage());

		return computers;
	}

	@Override
	public Integer getComputerCount(SqlRequestOptions sqlRequestOptions) {
		Integer i = null;

		i = computerDao.getComputerCount(sqlRequestOptions);

		// logger.warn("Erreur lors de la récupération du compte des ordinateurs"
		// + e.getMessage());
		return i;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteComputer(int id) {
		boolean res;
		Assert.isTrue(computerDao.isComputerExists(id),
				"l'id de l'ordinateur n'existe pas.");
		res = computerDao.deleteComputer(id);
		Assert.isTrue(res, "L'ordinateur n'a pas pu être supprimé");
	}

	@Override
	public Computer getComputer(int id) {
		Computer c = null;
		c = computerDao.getComputer(id);
		Assert.notNull(c, "L'ordinateur n'a pas pu être récupéré");

		return c;
	}

	@Override
	public List<Company> getCompanies() {
		List<Company> companies = null;

		companies = companyDao.getCompanies();

		Assert.notNull(companies,
				"Erreur lors de la récupération de la liste des sociétés");

		return companies;
	}

	@Override
	public boolean isComputerExists(int id) {
		boolean b = false;

		b = computerDao.isComputerExists(id);

		return b;
	}

	@Override
	public Page createPage(int page, int maxAffichage,
			SqlRequestOptions sqlRequestOptions) {
		int total = 0;
		List<Computer> computers = new ArrayList<Computer>();

		computers = computerDao.getComputers(page * maxAffichage, maxAffichage,
				sqlRequestOptions);
		total = computerDao.getComputerCount(sqlRequestOptions);

		if (page < 0 || (page > 0 && (total - page * maxAffichage < 0))) {
			page = 0;
		}
		return new Page(page, maxAffichage, total, computers);
	}

	@Override
	public PageEdition createPageEdition(int idComputer) {
		Computer computer = null;
		List<Company> companies = new ArrayList<Company>();

		Assert.isTrue(computerDao.isComputerExists(idComputer),
				"l'id de l'ordinateur n'existe pas.");

		computer = computerDao.getComputer(idComputer);
		companies = companyDao.getCompanies();

		return new PageEdition(computer, companies);
	}

}