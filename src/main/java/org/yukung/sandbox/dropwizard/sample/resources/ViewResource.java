package org.yukung.sandbox.dropwizard.sample.resources;

import com.google.common.base.Charsets;
import io.dropwizard.views.View;
import org.yukung.sandbox.dropwizard.sample.core.Person;
import org.yukung.sandbox.dropwizard.sample.views.PersonView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author yukung
 */
@Path("/views")
public class ViewResource {
    @GET
    @Produces("text/html;charset=UTF-8")
    @Path("/freemarker")
    public View freemarkerUTF8() {
        return new View("/views/ftl/utf8.ftl", Charsets.UTF_8) {
        };
    }
    @GET
    @Produces("text/html;charset=UTF-8")
    @Path("/mustache")
    public View mustacheUTF8() {
//        return new View("/views/mustache/utf8.mustache", Charsets.UTF_8) {
//        };
        Person person = new Person();
        person.setId(1);
        person.setFullName("Hoge Mustache");
        person.setJobTitle("mustache!");
        return new PersonView(PersonView.Template.MUSTACHE, person);
    }
}
