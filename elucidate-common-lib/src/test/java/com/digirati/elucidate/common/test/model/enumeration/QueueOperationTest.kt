package com.digirati.elucidate.common.test.model.enumeration

import com.digirati.elucidate.common.model.enumeration.QueueOperation
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class QueueOperationTest {
    @Test
    fun testQueueOperationCreate() {
        val queueOperation = QueueOperation.CREATE
        Assert.assertNotNull(queueOperation)
        MatcherAssert.assertThat(queueOperation, Matchers.`is`(Matchers.equalTo(QueueOperation.CREATE)))
    }

    @Test
    fun testQueueOperationUpdate() {
        val queueOperation = QueueOperation.UPDATE
        Assert.assertNotNull(queueOperation)
        MatcherAssert.assertThat(queueOperation, Matchers.`is`(Matchers.equalTo(QueueOperation.UPDATE)))
    }

    @Test
    fun testQueueOperationDelete() {
        val queueOperation = QueueOperation.DELETE
        Assert.assertNotNull(queueOperation)
        MatcherAssert.assertThat(queueOperation, Matchers.`is`(Matchers.equalTo(QueueOperation.DELETE)))
    }
}