package com.digirati.elucidate.common.test.model.annotation.oa

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotation
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationTest
import org.junit.Test

class OAAnnotationTest : AbstractAnnotationTest<OAAnnotation?>() {
    @Test
    fun testOAAnnotation() {
        val oaAnnotation = OAAnnotation()
        testAbstractAnnotation(oaAnnotation)
    }
}