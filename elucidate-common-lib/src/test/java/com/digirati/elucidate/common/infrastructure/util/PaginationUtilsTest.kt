package com.digirati.elucidate.common.infrastructure.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaginationUtilsTest {

    @Test
    public void testLastPage() {
        assertEquals(PaginationUtils.calculateLastPage(0, 100), 0);
        assertEquals(PaginationUtils.calculateLastPage(99, 100), 0);
        assertEquals(PaginationUtils.calculateLastPage(100, 100), 0);
        assertEquals(PaginationUtils.calculateLastPage(101, 100), 1);
        assertEquals(PaginationUtils.calculateLastPage(199, 100), 1);
        assertEquals(PaginationUtils.calculateLastPage(200, 100), 1);
        assertEquals(PaginationUtils.calculateLastPage(201, 100), 2);
    }

}