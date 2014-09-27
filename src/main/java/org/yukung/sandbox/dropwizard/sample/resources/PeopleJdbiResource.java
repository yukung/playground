package org.yukung.sandbox.dropwizard.sample.resources;

import com.sun.jersey.api.NotFoundException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import org.yukung.sandbox.dropwizard.sample.core.Person;
import org.yukung.sandbox.dropwizard.sample.db.PersonJdbiDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author yukung
 */
@Path("/people/jdbi")
@Produces(MediaType.APPLICATION_JSON)
public class PeopleJdbiResource {

    private final PersonJdbiDAO peopleDAO;

    public PeopleJdbiResource(PersonJdbiDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    @GET
    @UnitOfWork
    public List<Person> listPeople() {
        return peopleDAO.findAll();
    }

    @GET
    @UnitOfWork
    @Path("/{personId}")
    public Person getPerson(@PathParam("personId") LongParam personId) {
        final Person person = peopleDAO.findById(personId.get());
        if (person == null) {
            throw new NotFoundException("{\"status\":\"notfound\"}");
        }
        return person;
    }
}
