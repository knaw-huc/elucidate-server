package com.digirati.elucidate.common.service.impl

import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class IRIBuilderServiceImplTest {
    @Test
    fun testToString() {
        val since = Date()
        val dateString = IRIBuilderServiceImpl.toString(since)
        println(dateString)
        assertNotNull(dateString)
    }
}