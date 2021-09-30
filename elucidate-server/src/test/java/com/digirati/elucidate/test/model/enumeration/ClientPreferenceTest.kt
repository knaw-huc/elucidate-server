package com.digirati.elucidate.test.model.enumeration

import com.digirati.elucidate.model.enumeration.ClientPreference
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class ClientPreferenceTest {
    @Test
    fun testClientPreferenceMinimalContainer() {
        val clientPreference = ClientPreference.MINIMAL_CONTAINER
        Assert.assertNotNull(clientPreference)
        MatcherAssert.assertThat(clientPreference, Matchers.`is`(Matchers.equalTo(ClientPreference.MINIMAL_CONTAINER)))
    }

    @Test
    fun testClientPreferenceContainedIris() {
        val clientPreference = ClientPreference.CONTAINED_IRIS
        Assert.assertNotNull(clientPreference)
        MatcherAssert.assertThat(clientPreference, Matchers.`is`(Matchers.equalTo(ClientPreference.CONTAINED_IRIS)))
    }

    @Test
    fun testClientPreferenceContainedDescriptions() {
        val clientPreference = ClientPreference.CONTAINED_DESCRIPTIONS
        Assert.assertNotNull(clientPreference)
        MatcherAssert.assertThat(
            clientPreference,
            Matchers.`is`(Matchers.equalTo(ClientPreference.CONTAINED_DESCRIPTIONS))
        )
    }
}