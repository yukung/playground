package org.yukung.sandbox.dropwizard.sample.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.yukung.sandbox.dropwizard.sample.core.User;

/**
 * @author yukung
 */
public class SampleAuthenticator implements Authenticator<BasicCredentials, User> {
    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if ("user".equals(basicCredentials.getUsername()) && "pass".equals(basicCredentials.getPassword())) {
            return Optional.of(new User("yukung " + basicCredentials.getUsername()));
        }
        return Optional.absent();
    }
}
