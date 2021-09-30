package com.digirati.elucidate.common.test.model.annotation.w3c

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationTest
import org.junit.Test

class W3CAnnotationTest : AbstractAnnotationTest<W3CAnnotation?>() {
    @Test
    fun testW3CAnnotation() {
        val w3cAnnotation = W3CAnnotation()
        testAbstractAnnotation(w3cAnnotation)
    }
}