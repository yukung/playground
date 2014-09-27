package org.yukung.sandbox.dropwizard.sample.resources;

import com.google.common.base.Optional;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.yukung.sandbox.dropwizard.sample.core.Person;
import org.yukung.sandbox.dropwizard.sample.db.PersonDAO;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author yukung
 */
public class PeopleResourceTest {

    private static final PersonDAO dao = mock(PersonDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PeopleResource(dao)).build();

    private final Person person = new Person(1, "yukung", "job");

    @Before
    public void setUp() {
        when(dao.findById(Long.parseLong("1"))).thenReturn(Optional.fromNullable(person));
    }

    @Test
    public void testGetPerson() {
        assertThat(resources.client().resource("/people/1").get(Person.class)).isEqualsToByComparingFields(person);
        verify(dao).findById(Long.parseLong("1"));
    }
}
