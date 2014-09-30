package org.yukung.sandbox.dropwizard.sample.resources;

import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.yukung.sandbox.dropwizard.sample.SampleDomaConfig;
import org.yukung.sandbox.dropwizard.sample.core.Employee;
import org.yukung.sandbox.dropwizard.sample.db.EmployeeDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author yukung
 */
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private final EmployeeDao employeeDao;

    public EmployeeResource(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @GET
    @UnitOfWork
    public List<Employee> listEmployee() {
        LocalTransaction tx = SampleDomaConfig.getLocalTransaction();
        try {
            tx.begin();
            final Optional<List<Employee>> employees = Optional.fromNullable(employeeDao.selectAll());
            if (!employees.isPresent()) {
                throw new NotFoundException("{\"status\":\"notfound\"}");
            }
            tx.commit();
            return employees.get();
        } finally {
            tx.rollback();
        }
    }

    @GET
    @UnitOfWork
    @Path("/{employeeId}")
    public Employee getEmployee(@PathParam("employeeId") IntParam employeeId) {
        LocalTransaction tx = SampleDomaConfig.getLocalTransaction();
        try {
            tx.begin();
            final Optional<Employee> employee =
                    Optional.fromNullable(employeeDao.selectById(employeeId.get()));
            if (!employee.isPresent()) {
                throw new NotFoundException("{\"status\":\"notfound\"}");
            }
            tx.commit();
            return employee.get();
        } finally {
            tx.rollback();
        }
    }
}
