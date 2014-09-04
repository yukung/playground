package org.yukung.sandbox.mybatis.user.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yukung.sandbox.mybatis.user.Gender;
import org.yukung.sandbox.mybatis.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserDaoTest {

    private UserDao userDao = new UserDao();

    @Before
    public void setUp() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE user (id IDENTITY, name VARCHAR(255), age INTEGER, gender VARCHAR(32))");
        stmt.close();
        conn.close();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsertOneUser() throws Exception {
        List<User> actual1 = userDao.findAll();
        assertThat(actual1, is(notNullValue()));
        assertThat(actual1, hasSize(0));
        assertThat(actual1, empty());

        final String name = "anonymous";
        final int age = 20;
        final Gender gender = Gender.FEMALE;

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setGender(gender);
        int num = userDao.insert(user);
        assertThat(num, is(1));

        List<User> actual2 = userDao.findAll();
        assertThat(actual2, is(notNullValue()));
        assertThat(actual2, hasSize(1));
        User actualUser = actual2.get(0);
        assertThat(actualUser, is(notNullValue()));
        assertThat(actualUser.getId(), is(notNullValue()));
        assertThat(actualUser.getName(), is(equalTo(name)));
        assertThat(actualUser.getAge(), is(equalTo(age)));
        assertThat(actualUser.getGender(), is(equalTo(gender)));
        assertThat(actual2, is(not(equalTo(actual1))));
    }
}
