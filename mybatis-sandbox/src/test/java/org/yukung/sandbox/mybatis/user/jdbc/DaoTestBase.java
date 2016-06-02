package org.yukung.sandbox.mybatis.user.jdbc;

/**
 * @author yukung
 */
public class DaoTestBase {
    protected ConnectionFactory getConnectionFactory() {
        return new JdbcConnectionFactory("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
    }
}
