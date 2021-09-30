package com.digirati.elucidate.common.test.model.annotation

import com.digirati.elucidate.common.model.annotation.AbstractObject
import com.digirati.elucidate.common.test.AbstractTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertNotNull

abstract class AbstractObjectTest<T : AbstractObject?> : AbstractTest() {
    protected fun testAbstractObject(abstractObject: T) {
        assertNotNull(abstractObject)
        val createdDateTime = generateRandomDate()
        abstractObject!!.createdDateTime = createdDateTime
        assertThat(
            createdDateTime, Matchers.`is`(
                equalTo(
                    abstractObject.createdDateTime
                )
            )
        )
        val deleted = generateRandomBoolean()
        abstractObject.isDeleted = deleted
        assertThat(
            deleted, Matchers.`is`(
                equalTo(
                    abstractObject.isDeleted
                )
            )
        )
        val jsonMap = generateRandomJsonMap()
        abstractObject.jsonMap = jsonMap
        assertThat(
            jsonMap, Matchers.`is`(
                equalTo(
                    abstractObject.jsonMap
                )
            )
        )
        val modifiedDateTime = generateRandomDate()
        abstractObject.modifiedDateTime = modifiedDateTime
        assertThat(
            modifiedDateTime, Matchers.`is`(
                equalTo(
                    abstractObject.modifiedDateTime
                )
            )
        )
    }
}