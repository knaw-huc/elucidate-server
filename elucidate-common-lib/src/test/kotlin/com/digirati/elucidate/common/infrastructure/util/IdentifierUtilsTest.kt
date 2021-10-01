package com.digirati.elucidate.common.infrastructure.util

import com.digirati.elucidate.common.infrastructure.constants.ElucidateConstants
import org.junit.Test
import kotlin.test.assertEquals

class IdentifierUtilsTest {
    @Test
    fun trimIdentifier() {
        val id = "http://example.org/thisisaveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryverylongidentifier_itislongerthanelucidatecanhandle_sotrimit"
        val trimmed = IdentifierUtils.trimIdentifier(id);
        assertEquals(ElucidateConstants.MAX_ID_SIZE, trimmed.length)
    }
}