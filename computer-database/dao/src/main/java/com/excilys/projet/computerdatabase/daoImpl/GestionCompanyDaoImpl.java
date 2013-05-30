package com.excilys.projet.computerdatabase.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.excilys.projet.computerdatabase.dao.GestionCompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.projet.computerdatabase.model.Company;

@Repository
@Scope("singleton")
public class GestionCompanyDaoImpl implements GestionCompanyDao {
	
	/**
	 * Query
	 */
	private static final String SELECT_ALL_COMPANIES_QUERY = "select id, name from company order by name";
	private static final String SELECT_ONE_COMPANY_BY_ID_QUERY = "select id, name from company where id = ?";
	
	
	@Autowired
	private JdbcTemplate jt;

	
	
	@Override
	public List<Company> getCompanies() throws SQLException{
		List<Company> liste= jt.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement myPreparedStatement = con.prepareStatement(SELECT_ALL_COMPANIES_QUERY);
				return myPreparedStatement;
			}
		}, new RowCompany());
		
		return liste;
	}
	
	@Override
	public Company getCompany(final int id) throws SQLException{
		List<Company> liste= jt.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement myPreparedStatement = con.prepareStatement(SELECT_ONE_COMPANY_BY_ID_QUERY);
				myPreparedStatement.setInt(1, id);
				return myPreparedStatement;
			}
		}, new RowCompany());

		return liste.get(0);
	}
	
	class RowCompany implements RowMapper<Company>{
		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company c = new Company();
			c.setId(rs.getInt("id"));
			c.setName(rs.getString("name"));
			return c;
		}
	}
	
}
