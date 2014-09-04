package org.yukung.sandbox.mybatis.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Yusuke Ikeda
 */
public final class TestUtils {

    private TestUtils() {
    }

    public static void createTable() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE user (id IDENTITY, name VARCHAR(255), age INTEGER, gender VARCHAR(32))");
        stmt.close();
        conn.close();
    }

}
