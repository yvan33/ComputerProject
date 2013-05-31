package com.excilys.projet.computerdatabase.servlet;

import com.excilys.projet.computerdatabase.model.Computer;
import com.excilys.projet.computerdatabase.model.Page;
import com.excilys.projet.computerdatabase.model.PageEdition;
import com.excilys.projet.computerdatabase.model.SqlRequestOptions;
import com.excilys.projet.computerdatabase.service.GestionComputerService;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Controller
public class Controleur {

	@Autowired
    GestionComputerService service;
	private final static int MAX_AFFICHAGE = 10;
	private static final Logger logger = LoggerFactory.getLogger(Controleur.class);

	
	@RequestMapping("/affichageComputers")
	public String affichageComputers(ModelMap model,
			@RequestParam(value = "page", required = false) String pageValue,
			@RequestParam(value = "s", required = false) String sortValue,
			@RequestParam(value = "info", required = false) String info,
			@RequestParam(value = "f", required = false) String filter) {

		Page page = null;
		Integer pageNumber = 0;
		if (pageValue != null) {
			try {
				pageNumber = Integer.parseInt(pageValue);
			} catch (NumberFormatException e) {
				pageNumber = 0;
			}
		}
		int sort = 1;
		try {
			sort = Integer.parseInt(sortValue);
		} catch (NumberFormatException e) {
			sort = 2;
		}
		try {
			page = service.createPage(pageNumber, MAX_AFFICHAGE,
					new SqlRequestOptions(filter, sort));
			if (!StringUtils.isNullOrEmpty(info)) {
				model.addAttribute("info", info);
			}
			model.addAttribute("page", page);
			model.addAttribute("tri", sort);
			model.addAttribute("filter", filter);

			return "affichageComputers";
		} catch (SQLException e) {
			model.addAttribute("error", "Erreur technique");
			return "errorPage";
		}

	}

	@RequestMapping("ajoutComputer.html")
	public String ajoutComputerServlet(ModelMap model) {
		try {
			model.addAttribute("companies", service.getCompanies());
			return "ajoutComputer";
		} catch (SQLException e) {
			model.addAttribute("error", "Erreur technique.");
			return "errorPage";
		}

	}

	@RequestMapping("editionComputer.html")
	public String editionComputer(ModelMap model,
			@RequestParam(value = "id", required = false) Integer id) {
		
		try{
			PageEdition pageEdition = service.createPageEdition(id);
			model.addAttribute("computer", pageEdition.getComputer());
			model.addAttribute("companies", pageEdition.getCompanies());
			
			return "editionComputer";
		}catch(NumberFormatException e){
			return "affichageComputers";
		} catch (SQLException e) {
			model.addAttribute("error", "Erreur technique.");
			return "errorPage";
			} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			model.addAttribute("error", e.getMessage());
			return "errorPage";
		}
	}
	
	@RequestMapping("delete.html")
	public String delete(ModelMap model,
			@RequestParam(value = "id", required = false) Integer id) {
		
		try {
			service.deleteComputer(id);
			model.addAttribute("info","Computer has been deleted");
			return "redirect:affichageComputers.html";
		} catch (SQLException e) {
			model.addAttribute("error", "Erreur technique.");
			return "errorPage";
		} catch (NumberFormatException e) {
			model.addAttribute("error", e.getMessage());
			return "errorPage";
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			model.addAttribute("error", e.getMessage());
			return "errorPage";
		}
	}


	@RequestMapping("validation.html")
	public String validation(ModelMap model,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "introduced", required = false) String introduced,
			@RequestParam(value = "discontinued", required = false) String discontinued,
			@RequestParam(value = "company", required = false) String company) 
	{

		boolean error = false;
		Computer computer = new Computer();

		// Check de l'id
		if (id != null) {
			computer.setId(id);
		}

		// Check du nom de l'ordinateur
		if (name == null || name.trim().length() == 0) {
			error = true;
			model.addAttribute("nameError", "error");
		} else {
			computer.setName(name);
		}

		// Check des dates
		SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
		df.applyPattern("yyyy-MM-dd");
		df.setLenient(false);

		if (introduced == null || introduced.isEmpty()) {
			computer.setIntroduced(null);
		} else {
			try {
				computer.setIntroduced(df.parse(introduced));
			} catch (ParseException e) {
				error = true;
				model.addAttribute("introducedError", "error");
			}
		}

		if (discontinued == null || discontinued.isEmpty()) {
			computer.setDiscontinued(null);
		} else {
			try {
				computer.setDiscontinued(df.parse(discontinued));
			} catch (ParseException e) {
				error = true;
				model.addAttribute("discontinuedError", "error");
			}
		}
		try {
			// Check de la compagnie
			if (!StringUtils.isNullOrEmpty(company)) {
				computer.setCompany(service.getCompany(Integer
						.parseInt(company)));
			}
			if (!error) {
				service.insertOrUpdate(computer);
				StringBuilder sb = new StringBuilder("Computer ").append(
						computer.getName()).append(" has been ");
				if (id != null) {
					sb.append("updated");
				} else {
					sb.append("created");
				}
				model.addAttribute("info", sb.toString());
				return "redirect:affichageComputers.html";
			} else {
				model.addAttribute("computer", computer);
				model.addAttribute("companies", service.getCompanies());
				if (id != null) {
					return "editionComputer";
				} else {
					return "ajoutComputer";
				}
			}
		} catch (SQLException e) {
			model.addAttribute("error", "Erreur technique");
			return "errorPage";
		} catch (IllegalArgumentException e) {
			logger.warn(e.getMessage());
			model.addAttribute("error", "L'ordinateur n'existe pas.");
			return "errorPage";
		}
	}
	

}
