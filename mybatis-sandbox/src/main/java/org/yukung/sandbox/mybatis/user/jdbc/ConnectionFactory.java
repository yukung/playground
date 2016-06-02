package org.yukung.sandbox.mybatis.user.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yukung
 */
public interface ConnectionFactory {
    Connection getConnection() throws SQLException;
}
