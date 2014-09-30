package org.yukung.sandbox.dropwizard.sample.db;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.yukung.sandbox.dropwizard.sample.SampleApplication;
import org.yukung.sandbox.dropwizard.sample.SampleConfiguration;
import org.yukung.sandbox.dropwizard.sample.SampleDomaConfig;
import org.yukung.sandbox.dropwizard.sample.core.Employee;
import org.yukung.sandbox.dropwizard.sample.db.impl.EmployeeDaoImpl;

/**
 * @author yukung
 */
public class EmployeeDaoTest {

	@ClassRule
	public static final DropwizardAppRule<SampleConfiguration> RULE =
			new DropwizardAppRule<>(SampleApplication.class, "sample.yml");

	private static Database database;

	private Liquibase migrations;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		database = createDatabase();
	}

	private static Database createDatabase() throws SQLException, DatabaseException {
		DataSourceFactory dataSourceFactory = RULE.getConfiguration().getDataSourceFactory();
		Properties info = new Properties();
		info.setProperty("user", dataSourceFactory.getUser());
		info.setProperty("password", dataSourceFactory.getPassword());
		org.h2.jdbc.JdbcConnection h2Conn =
				new org.h2.jdbc.JdbcConnection(dataSourceFactory.getUrl(), info);
		JdbcConnection conn = new JdbcConnection(h2Conn);
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);
		return database;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		database.close();
		database = null;
	}

	@Before
	public void setUp() throws Exception {
		migrations = createLiquibase("migrations.xml");
		String context = null;
		migrations.update(context);
	}

	@After
	public void tearDown() throws Exception {
		migrations.dropAll();
	}

	private Liquibase createLiquibase(String migration) throws LiquibaseException {
		Liquibase liquibase = new Liquibase(migration, new ClassLoaderResourceAccessor(), database);
		return liquibase;
	}

	@Test
	public void testGetAndUpdate() throws LiquibaseException {
		Liquibase data = createLiquibase("data.xml");
		data.update("testGetAndUpdateEmployee");

		LocalTransaction tx = SampleDomaConfig.getLocalTransaction();
		try {
			tx.begin();
			EmployeeDao dao = new EmployeeDaoImpl();
			Employee employee = dao.selectById(1);

			assertThat(employee, is(notNullValue()));
			assertThat(employee.employeeName, is("SMITH"));
			LocalDate localDate = new LocalDate(1980, 12, 17);
			Date expectedDate = new Date(localDate.toDate().getTime());
			assertThat(employee.hiredate, is(expectedDate));
			assertThat(employee.salary, is(new BigDecimal("800.00")));

			employee.employeeName = "KING";
			employee.salary = employee.salary.add(new BigDecimal("1000.00"));
			dao.update(employee);

			Employee updatedEmployee = dao.selectById(1);
			tx.commit();
			assertThat(updatedEmployee, is(notNullValue()));
			assertThat(updatedEmployee.employeeName, is("KING"));
			assertThat(updatedEmployee.hiredate, is(expectedDate));
			assertThat(updatedEmployee.salary, is(new BigDecimal("1800.00")));
			assertThat(updatedEmployee.versionNo, is(2));
		} finally {
			tx.rollback();
		}
	}

}
