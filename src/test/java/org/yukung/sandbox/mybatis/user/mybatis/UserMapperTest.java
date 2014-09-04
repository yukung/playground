package org.yukung.sandbox.mybatis.user.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yukung.sandbox.mybatis.user.Gender;
import org.yukung.sandbox.mybatis.user.TestUtils;
import org.yukung.sandbox.mybatis.user.User;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author yukung
 */
public class UserMapperTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp() throws Exception {
        TestUtils.createTable();
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsertOneUserWithoutMapper() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> actual1 = sqlSession.selectList("org.yukung.sandbox.mybatis.user.mybatis.UserMapper.findAll");
            assertThat(actual1, is(notNullValue()));
            assertThat(actual1, hasSize(0));
            assertThat(actual1, empty());

            final String name = "John Doe";
            final int age = 43;
            final Gender gender = Gender.MALE;

            User user = new User();
            user.setName(name);
            user.setAge(age);
            user.setGender(gender);
            int num = sqlSession.insert("org.yukung.sandbox.mybatis.user.mybatis.UserMapper.insert", user);
            assertThat(num, is(1));

            List<User> actual2 = sqlSession.selectList("org.yukung.sandbox.mybatis.user.mybatis.UserMapper.findAll");
            assertThat(actual2, is(notNullValue()));
            assertThat(actual2, hasSize(1));
            User actualUser1 = actual2.get(0);
            assertThat(actualUser1, is(notNullValue()));
            assertThat(actualUser1.getId(), is(notNullValue()));
            assertThat(actualUser1.getName(), is(equalTo(name)));
            assertThat(actualUser1.getAge(), is(equalTo(age)));
            assertThat(actualUser1.getGender(), is(equalTo(gender)));
            assertThat(actual2, is(not(equalTo(actual1))));

            Long id = actualUser1.getId();
            User actualUser2 = sqlSession.selectOne("org.yukung.sandbox.mybatis.user.mybatis.UserMapper.findById", id);
            assertThat(actualUser2, is(notNullValue()));
            assertThat(actualUser2.getId(), is(equalTo(actualUser1.getId())));
            assertThat(actualUser2.getName(), is(equalTo(actualUser1.getName())));
            assertThat(actualUser2.getAge(), is(equalTo(actualUser1.getAge())));
            assertThat(actualUser2.getGender(), is(equalTo(actualUser1.getGender())));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsertOneUserWithMapper() throws Exception {


    }
}
