package com.digirati.elucidate.common.service.impl;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IRIBuilderServiceImplTest {

    @Test
    public void testToString() {
        Date since = new Date();
        String dateString = IRIBuilderServiceImpl.toString(since);
        System.out.println(dateString);
        assertNotNull(dateString);
    }
}