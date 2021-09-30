package com.digirati.elucidate.test.model

import com.digirati.elucidate.common.test.AbstractTest
import com.digirati.elucidate.model.ServiceResponse
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class ServiceResponseTest : AbstractTest() {
    @Test
    fun testOk() {
        val status = ServiceResponse.Status.OK
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.OK)))
    }

    @Test
    fun testNotFound() {
        val status = ServiceResponse.Status.NOT_FOUND
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.NOT_FOUND)))
    }

    @Test
    fun testCacheKeyMiss() {
        val status = ServiceResponse.Status.CACHE_KEY_MISS
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.CACHE_KEY_MISS)))
    }

    @Test
    fun testIllegalModification() {
        val status = ServiceResponse.Status.ILLEGAL_MODIFICATION
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.ILLEGAL_MODIFICATION)))
    }

    @Test
    fun testNonConformant() {
        val status = ServiceResponse.Status.NON_CONFORMANT
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.NON_CONFORMANT)))
    }

    @Test
    fun testDeleted() {
        val status = ServiceResponse.Status.DELETED
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.DELETED)))
    }

    @Test
    fun testAlreadyExists() {
        val status = ServiceResponse.Status.ALREADY_EXISTS
        Assert.assertNotNull(status)
        MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.ALREADY_EXISTS)))
    }

    @Test
    fun testServiceResponse() {
        for (status in ServiceResponse.Status.values()) {
            val w3cAnnotation = generateRandomW3CAnnotation()
            val serviceResponse = ServiceResponse(status, w3cAnnotation)
            MatcherAssert.assertThat(w3cAnnotation, Matchers.`is`(Matchers.equalTo(serviceResponse.obj)))
            MatcherAssert.assertThat(status, Matchers.`is`(Matchers.equalTo(serviceResponse.status)))
        }
    }
}