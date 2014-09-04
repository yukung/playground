package org.yukung.sandbox.mybatis.user.mybatis;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * @author Yusuke Ikeda
 */
public final class MyBatisUtils {

    private MyBatisUtils() {
    }

    public static Configuration getConfiguration() {
        Environment environment = new Environment(
                "development",
                new JdbcTransactionFactory(),
                new PooledDataSource("org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
        );
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        return configuration;
    }
}
