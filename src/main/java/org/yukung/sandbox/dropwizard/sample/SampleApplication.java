package org.yukung.sandbox.dropwizard.sample;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.yukung.sandbox.dropwizard.sample.auth.SampleAuthenticator;
import org.yukung.sandbox.dropwizard.sample.health.TemplateHealthCheck;
import org.yukung.sandbox.dropwizard.sample.resources.HelloWorldResource;
import org.yukung.sandbox.dropwizard.sample.resources.ProtectedResource;
import org.yukung.sandbox.dropwizard.sample.resources.ViewResource;

/**
 * @author yukung
 */
public class SampleApplication extends Application<SampleConfiguration> {

	public static void main(String[] args) throws Exception {
		new SampleApplication().run(args);
	}

	@Override
	public String getName() {
		return "Sample Application";
	}

	@Override
	public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new ViewBundle());
    }

	@Override
	public void run(SampleConfiguration configuration, Environment environment) throws Exception {
		final HelloWorldResource resource =
				new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
		final TemplateHealthCheck healthCheck =
				new TemplateHealthCheck(configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(resource);
        environment.jersey().register(new ViewResource());
        environment.jersey().register(new BasicAuthProvider<>(new SampleAuthenticator(), "SUPER SECRET STUFF"));
        environment.jersey().register(new ProtectedResource());
	}

}
