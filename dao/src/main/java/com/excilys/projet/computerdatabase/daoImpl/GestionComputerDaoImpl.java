package com.excilys.projet.computerdatabase.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import com.excilys.projet.computerdatabase.dao.GestionComputerDao;
import com.excilys.projet.computerdatabase.model.SqlRequestOptions;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.CreateKeySecondPass;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.projet.computerdatabase.model.Company;
import com.excilys.projet.computerdatabase.model.Computer;

@Repository
public class GestionComputerDaoImpl implements GestionComputerDao {

	/**
	 * Query
	 */
	private static final String SELECT_ALL_COMPUTERS_QUERY = "select cpu.id, cpu.name, cpu.introduced, cpu.discontinued, cpy.id, cpy.name  from computer cpu left join company cpy on cpu.company_id=cpy.id";
	private static final String SELECT_ONE_COMPUTER_BY_ID_QUERY = "select cpu.id, cpu.name, cpu.introduced, cpu.discontinued, cpy.id, cpy.name  from computer cpu left join company cpy on cpu.company_id=cpy.id where cpu.id = ?";
	private static final String INSERT_COMPUTER = "insert into computer (name, introduced, discontinued, company_id) values (?,?,?,?)";
	private static final String DELETE_COMPUTER = "delete from computer where id=?";
	private static final String UPDATE_COMPUTER = "update computer set name = ?, introduced = ?, discontinued = ?, company_id = ? where id =? ";
	private static final String COUNT_COMPUTER = "select count(cpu.id) as count from computer cpu";
	private static final String ID_EXISTS_QUERY = "select count(id) as count from computer where id = ?";
	private static final String SELECT_WHERE = " where cpu.name LIKE ?";
	private static final String SELECT_ORDER_BY = " order by ISNULL (%1$s),%1$s %2$s limit %3$s, %4$s";

	@Autowired
	private JdbcTemplate jt;
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Computer> getComputers(final int debut, final int nombre,
			final SqlRequestOptions sqlRequestOptions) {
		Session session = sessionFactory.getCurrentSession();

		List<Computer> liste = new ArrayList<Computer>();

		Criteria criteria = session.createCriteria(Computer.class);
		if (sqlRequestOptions.getFilter() != null
				&& !sqlRequestOptions.getFilter().isEmpty())
			criteria.add(Restrictions.like("name",
					"%" + sqlRequestOptions.getFilter() + "%"));
		criteria.setMaxResults(nombre);
		criteria.setFirstResult(debut);

		Criteria criteria2;

		if (sqlRequestOptions.getSqlOrder().equals("ASC")) {

			if (!sqlRequestOptions.getSqlTri().equals("companyName"))
				criteria.addOrder(Order.asc(sqlRequestOptions.getSqlTri()));

			else {
				criteria2 = criteria.createCriteria("company");
				criteria2.addOrder(Order.asc("name"));
			}
		} else if (!sqlRequestOptions.getSqlTri().equals("companyName"))
			criteria.addOrder(Order.desc(sqlRequestOptions.getSqlTri()));

		else {
			criteria2 = criteria.createCriteria("company");
			criteria2.addOrder(Order.desc("name"));
		}

		liste = (List<Computer>) criteria.list();

		return liste;
	}


	@Override
	public Computer getComputer(final int id) {
		List<Computer> liste = new ArrayList<Computer>();
		liste = jt.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = con
						.prepareStatement(SELECT_ONE_COMPUTER_BY_ID_QUERY);
				myPreparedStatement.setInt(1, id);
				return myPreparedStatement;
			}
		}, new RowComputer());
		return liste.get(0);
	}

	@Override
	public Integer getComputerCount(final SqlRequestOptions sqlRequestOptions) {

		Integer count = jt.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = null;
				StringBuilder sb = new StringBuilder(COUNT_COMPUTER);
				if (sqlRequestOptions.getFilter() != null
						&& !sqlRequestOptions.getFilter().isEmpty()) {
					sb.append(SELECT_WHERE);
				}
				myPreparedStatement = con.prepareStatement(sb.toString());
				if (sqlRequestOptions.getFilter() != null
						&& !sqlRequestOptions.getFilter().isEmpty()) {
					myPreparedStatement.setString(1,
							sqlRequestOptions.getSqlFilter());
				}

				return myPreparedStatement;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				rs.first();
				return rs.getInt("count");
			}
		});
		return count;
	}

	@Override
	public boolean deleteComputer(final int id) {
		Session session = sessionFactory.getCurrentSession();
		List<Computer> computers = session.createCriteria(Computer.class)
				.add((Restrictions.idEq(id))).list();
		System.out.println(computers.get(0));

		int res = jt.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = con
						.prepareStatement(DELETE_COMPUTER);
				myPreparedStatement.setInt(1, id);
				return myPreparedStatement;
			}
		});
		return (res > 0) ? true : false;
	}

	@Override
	public boolean updateComputer(final Computer computer) {
		int res = jt.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = con
						.prepareStatement(UPDATE_COMPUTER);
				myPreparedStatement.setString(1, computer.getName());
				if (computer.getIntroduced() != null)
					myPreparedStatement.setDate(2, new java.sql.Date(computer
							.getIntroduced().getTime()));
				else
					myPreparedStatement.setDate(2, null);
				if (computer.getDiscontinued() != null)
					myPreparedStatement.setDate(3, new java.sql.Date(computer
							.getDiscontinued().getTime()));
				else
					myPreparedStatement.setDate(3, null);

				if (computer.getCompany() == null) {
					myPreparedStatement.setNull(4, Types.NULL);
				} else {
					myPreparedStatement
							.setInt(4, computer.getCompany().getId());
				}
				myPreparedStatement.setInt(5, computer.getId());
				return myPreparedStatement;
			}
		});
		return (res != 0) ? true : false;
	}

	@Override
	public boolean insertComputer(final Computer computer) {
		int count = jt.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = con
						.prepareStatement(INSERT_COMPUTER);
				myPreparedStatement.setString(1, computer.getName());
				if (computer.getIntroduced() != null) {
					myPreparedStatement.setDate(2, new java.sql.Date(computer
							.getIntroduced().getTime()));
				} else {
					myPreparedStatement.setDate(2, null);
				}
				if (computer.getDiscontinued() != null) {
					myPreparedStatement.setDate(3, new java.sql.Date(computer
							.getDiscontinued().getTime()));
				} else {
					myPreparedStatement.setDate(3, null);
				}
				if (computer.getCompany() == null) {
					myPreparedStatement.setNull(4, Types.NULL);
				} else {
					myPreparedStatement
							.setInt(4, computer.getCompany().getId());
				}
				return myPreparedStatement;
			}
		});
		return (count != 0) ? true : false;
	}

	@Override
	public boolean isComputerExists(final int id) {
		int count = jt.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement myPreparedStatement = con
						.prepareStatement(ID_EXISTS_QUERY);
				myPreparedStatement.setInt(1, id);
				return myPreparedStatement;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				rs.first();
				return rs.getInt("count");
			}
		});
		if (count == 1)
			return true;

		return false;
	}

	class RowComputer implements RowMapper<Computer> {
		@Override
		public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Computer c = new Computer();
			c.setId(rs.getInt("cpu.id"));
			c.setName(rs.getString("cpu.name"));
			c.setIntroduced(rs.getDate("cpu.introduced"));
			c.setDiscontinued(rs.getDate("cpu.discontinued"));
			Company cpy = new Company();
			cpy.setId(rs.getInt("cpy.id"));
			cpy.setName(rs.getString("cpy.name"));
			c.setCompany(cpy);
			return c;
		}
	}
}
