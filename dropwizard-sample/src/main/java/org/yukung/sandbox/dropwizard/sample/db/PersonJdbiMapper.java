package org.yukung.sandbox.dropwizard.sample.db;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.yukung.sandbox.dropwizard.sample.core.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author yukung
 */
public class PersonJdbiMapper implements ResultSetMapper<Person> {
    @Override
    public Person map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setFullName(resultSet.getString("fullName"));
        person.setJobTitle(resultSet.getString("jobTitle"));
        return person;
    }
}
