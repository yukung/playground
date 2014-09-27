package org.yukung.sandbox.dropwizard.sample.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.yukung.sandbox.dropwizard.sample.SampleApplication;
import org.yukung.sandbox.dropwizard.sample.SampleConfiguration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static org.fest.assertions.api.Assertions.*;

/**
 * @author yukung
 */
public class PeopleResourceIntegrationTest {

    private static final String FIXTURE_PATH = "fixtures/integration/";

    @ClassRule
    public static final DropwizardAppRule<SampleConfiguration> RULE = new DropwizardAppRule<>(SampleApplication.class, "sample.yml");

    private static Database database;

    private Liquibase migrations;

    @BeforeClass
    public static void beforeClass() throws SQLException, LiquibaseException {
        database = createDatabase();
    }

    private static Database createDatabase() throws SQLException, DatabaseException {
        DataSourceFactory dataSourceFactory = RULE.getConfiguration().getDataSourceFactory();
        Properties info = new Properties();
        info.setProperty("user", dataSourceFactory.getUser());
        info.setProperty("password", dataSourceFactory.getPassword());
        org.h2.jdbc.JdbcConnection h2Conn = new org.h2.jdbc.JdbcConnection(dataSourceFactory.getUrl(), info);
        JdbcConnection conn = new JdbcConnection(h2Conn);
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);
        return database;
    }

    @AfterClass
    public static void afterClass() throws DatabaseException, LockException {
        database.close();
        database = null;
    }

    @Before
    public void before() throws LiquibaseException {
        migrations = createLiquibase("migrations.xml");
        String context = null;
        migrations.update(context);
    }

    @After
    public void after() throws DatabaseException, LockException {
        migrations.dropAll();
    }

    private Liquibase createLiquibase(String migration) throws LiquibaseException {
        Liquibase liquibase = new Liquibase(migration, new ClassLoaderResourceAccessor(), database);
        return liquibase;
    }

    @Test
    public void testGetPerson() throws JsonParseException, JsonMappingException, IOException, LiquibaseException {
        Liquibase data = createLiquibase("data.xml");
        data.update("testGetPerson");

        Client client = new Client();

        ClientResponse response = client.resource(String.format("http://localhost:%d/people/2", RULE.getLocalPort())).get(ClientResponse.class);

        assertThat(response.getStatus()).isEqualTo(200);

        String entity = response.getEntity(String.class);
        assertThat(entity).isEqualTo(FixtureHelpers.fixture(FIXTURE_PATH + "getPerson.json"));
    }
}
