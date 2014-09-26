package org.yukung.sandbox.dropwizard.sample;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.yukung.sandbox.dropwizard.sample.health.TemplateHealthCheck;
import org.yukung.sandbox.dropwizard.sample.resources.HelloWorldResource;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void run(SampleConfiguration configuration, Environment environment) throws Exception {
		final HelloWorldResource resource =
				new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
		final TemplateHealthCheck healthCheck =
				new TemplateHealthCheck(configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(resource);
	}

}
