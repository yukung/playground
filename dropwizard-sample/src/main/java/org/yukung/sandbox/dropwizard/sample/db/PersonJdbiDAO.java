package org.yukung.sandbox.dropwizard.sample.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.yukung.sandbox.dropwizard.sample.core.Person;

import java.util.List;

/**
 * @author yukung
 */
public interface PersonJdbiDAO {

    @SqlQuery("select id, fullName, jobTitle from people where id = :id")
    @Mapper(PersonJdbiMapper.class)
    public Person findById(@Bind("id") Long id);

    @SqlQuery("select id, fullName, jobTitle from people")
    @Mapper(PersonJdbiMapper.class)
    public List<Person> findAll();
}
