package org.yukung.sandbox.mybatis.user.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.yukung.sandbox.mybatis.user.User;

import java.util.List;

/**
 * @author yukung
 */
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user (name, age, gender) VALUES (#{name}, #{age}, #{gender})")
    int insert(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
}
