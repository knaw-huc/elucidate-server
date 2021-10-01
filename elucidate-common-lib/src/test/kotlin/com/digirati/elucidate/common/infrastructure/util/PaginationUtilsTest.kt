package com.digirati.elucidate.common.infrastructure.util

import com.digirati.elucidate.common.infrastructure.util.PaginationUtils.calculateLastPage
import org.junit.Assert.assertEquals
import org.junit.Test

class PaginationUtilsTest {
    @Test
    fun testLastPage() {
        assertEquals(calculateLastPage(0, 100), 0)
        assertEquals(calculateLastPage(99, 100), 0)
        assertEquals(calculateLastPage(100, 100), 0)
        assertEquals(calculateLastPage(101, 100), 1)
        assertEquals(calculateLastPage(199, 100), 1)
        assertEquals(calculateLastPage(200, 100), 1)
        assertEquals(calculateLastPage(201, 100), 2)
    }
}