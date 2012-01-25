package org.yukung.sample.guice;

import static com.google.inject.matcher.Matchers.*;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(Service.class).to(ServiceImpl.class).in(Scopes.SINGLETON);
		bindInterceptor(
		// only(Client.class),
			identicalTo(Client.class),
			any(),
			new LoggingInterceptor());
	}

}
