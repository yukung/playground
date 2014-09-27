package org.yukung.sandbox.dropwizard.sample;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.skife.jdbi.v2.DBI;
import org.yukung.sandbox.dropwizard.sample.auth.SampleAuthenticator;
import org.yukung.sandbox.dropwizard.sample.core.Person;
import org.yukung.sandbox.dropwizard.sample.db.PersonDAO;
import org.yukung.sandbox.dropwizard.sample.db.PersonJdbiDAO;
import org.yukung.sandbox.dropwizard.sample.health.TemplateHealthCheck;
import org.yukung.sandbox.dropwizard.sample.resources.HelloWorldResource;
import org.yukung.sandbox.dropwizard.sample.resources.PeopleJdbiResource;
import org.yukung.sandbox.dropwizard.sample.resources.PeopleResource;
import org.yukung.sandbox.dropwizard.sample.resources.ProtectedResource;
import org.yukung.sandbox.dropwizard.sample.resources.ViewResource;

/**
 * @author yukung
 */
public class SampleApplication extends Application<SampleConfiguration> {

    private final HibernateBundle<SampleConfiguration> hibernateBundle = new HibernateBundle<SampleConfiguration>(Person.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(SampleConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

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
        bootstrap.addBundle(new MigrationsBundle<SampleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(SampleConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
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
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new PeopleResource(dao));
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "jdbi");
        PersonJdbiDAO jdbiDAO = jdbi.onDemand(PersonJdbiDAO.class);
        environment.jersey().register(new PeopleJdbiResource(jdbiDAO));
    }

}
