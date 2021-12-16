package com.digirati.elucidate.test.model

import com.digirati.elucidate.common.test.AbstractTest
import com.digirati.elucidate.model.ServiceResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Test

class ServiceResponseTest : AbstractTest() {
    @Test
    fun testOk() {
        val status = ServiceResponse.Status.OK
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.OK)))
    }

    @Test
    fun testNotFound() {
        val status = ServiceResponse.Status.NOT_FOUND
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.NOT_FOUND)))
    }

    @Test
    fun testCacheKeyMiss() {
        val status = ServiceResponse.Status.CACHE_KEY_MISS
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.CACHE_KEY_MISS)))
    }

    @Test
    fun testIllegalModification() {
        val status = ServiceResponse.Status.ILLEGAL_MODIFICATION
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.ILLEGAL_MODIFICATION)))
    }

    @Test
    fun testNonConformant() {
        val status = ServiceResponse.Status.NON_CONFORMANT
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.NON_CONFORMANT)))
    }

    @Test
    fun testDeleted() {
        val status = ServiceResponse.Status.DELETED
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.DELETED)))
    }

    @Test
    fun testAlreadyExists() {
        val status = ServiceResponse.Status.ALREADY_EXISTS
        assertNotNull(status)
        assertThat(status, `is`(equalTo(ServiceResponse.Status.ALREADY_EXISTS)))
    }

    @Test
    fun testServiceResponse() {
        for (status in ServiceResponse.Status.values()) {
            val w3cAnnotation = generateRandomW3CAnnotation()
            val serviceResponse = ServiceResponse(status, w3cAnnotation)
            assertThat(w3cAnnotation, `is`(equalTo(serviceResponse.obj)))
            assertThat(status, `is`(equalTo(serviceResponse.status)))
        }
    }
}