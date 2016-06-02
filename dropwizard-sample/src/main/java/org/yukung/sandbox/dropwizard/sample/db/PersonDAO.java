package org.yukung.sandbox.dropwizard.sample.db;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.yukung.sandbox.dropwizard.sample.core.Person;

import java.util.List;

/**
 * @author yukung
 */
public class PersonDAO extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("org.yukung.sandbox.dropwizard.sample.core.Person.findAll"));
    }
}
