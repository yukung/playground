package org.yukung.sandbox.dropwizard.sample.resources;

import io.dropwizard.auth.Auth;
import org.yukung.sandbox.dropwizard.sample.core.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author yukung
 */
@Path("/protected")
@Produces(MediaType.TEXT_PLAIN)
public class ProtectedResource {
    @GET
    public String showSecret(@Auth User user) {
        return String.format("Hey there, %s. You know the secret!", user.getName());
    }
}
