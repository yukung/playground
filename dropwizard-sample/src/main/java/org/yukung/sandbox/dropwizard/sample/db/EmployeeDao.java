package org.yukung.sandbox.dropwizard.sample.db;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.yukung.sandbox.dropwizard.sample.SampleDomaConfig;
import org.yukung.sandbox.dropwizard.sample.core.Employee;

import java.util.List;

@Dao(config = SampleDomaConfig.class)
public interface EmployeeDao {

    @Select
    Employee selectById(Integer employeeId);

    @Select
    List<Employee> selectAll();

    @Update
    int update(Employee employee);
}
