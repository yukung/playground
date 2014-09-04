package org.yukung.sandbox.mybatis.user.mybatis;

import org.yukung.sandbox.mybatis.user.User;

import java.util.List;

/**
 * @author yukung
 */
public interface UserMapper {

    List<User> findAll();

    int insert(User user);

    User findById(Long id);
}
