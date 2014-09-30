package org.yukung.sandbox.dropwizard.sample;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

public class SampleDomaConfig extends DomaAbstractConfig {

	protected static final LocalTransactionalDataSource dataSource = createDateSource();

	protected static final Dialect dialect = new H2Dialect();

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public Dialect getDialect() {
		return dialect;
	}

	protected static LocalTransactionalDataSource createDateSource() {
		SimpleDataSource dataSource = new SimpleDataSource();
		dataSource.setUrl("jdbc:h2:./target/sample");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return new LocalTransactionalDataSource(dataSource);
	}

	public static LocalTransaction getLocalTransaction() {
		return dataSource.getLocalTransaction(defaultJdbcLogger);
	}
}
