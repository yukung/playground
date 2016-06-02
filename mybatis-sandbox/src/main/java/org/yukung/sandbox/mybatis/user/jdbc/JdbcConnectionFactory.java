package org.yukung.sandbox.mybatis.user.jdbc;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author yukung
 */
@AllArgsConstructor
public class JdbcConnectionFactory implements ConnectionFactory {

    private String url;
    private String username;
    private String password;

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
