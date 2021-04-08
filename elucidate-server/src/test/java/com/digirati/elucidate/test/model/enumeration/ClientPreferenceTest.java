package com.digirati.elucidate.test.model.enumeration;

import com.digirati.elucidate.model.enumeration.ClientPreference;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class ClientPreferenceTest {

    @Test
    public void testClientPreferenceMinimalContainer() {
        ClientPreference clientPreference = ClientPreference.MINIMAL_CONTAINER;
        assertNotNull(clientPreference);
        assertThat(clientPreference, is(equalTo(ClientPreference.MINIMAL_CONTAINER)));
    }

    @Test
    public void testClientPreferenceContainedIris() {
        ClientPreference clientPreference = ClientPreference.CONTAINED_IRIS;
        assertNotNull(clientPreference);
        assertThat(clientPreference, is(equalTo(ClientPreference.CONTAINED_IRIS)));
    }

    @Test
    public void testClientPreferenceContainedDescriptions() {
        ClientPreference clientPreference = ClientPreference.CONTAINED_DESCRIPTIONS;
        assertNotNull(clientPreference);
        assertThat(clientPreference, is(equalTo(ClientPreference.CONTAINED_DESCRIPTIONS)));
    }
}
