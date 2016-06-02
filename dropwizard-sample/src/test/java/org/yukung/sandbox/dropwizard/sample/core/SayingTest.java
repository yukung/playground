package org.yukung.sandbox.dropwizard.sample.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.*;

/**
 * @author yukung
 */
public class SayingTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final String FIXTURE = "fixtures/saying.json";

    @Test
    public void deserializeFromJSON() throws Exception {
        final Saying target = new Saying(1, "Hello");
        final Saying expected = MAPPER.readValue(FixtureHelpers.fixture(FIXTURE), Saying.class);
        assertThat(target).isEqualsToByComparingFields(expected);
    }

    @Test
    public void serializeToJSON() throws Exception {
        final Saying target = new Saying(1, "Hello");
        final String actual = MAPPER.writeValueAsString(target);
        assertThat(actual).isEqualTo(FixtureHelpers.fixture(FIXTURE));
    }
}
