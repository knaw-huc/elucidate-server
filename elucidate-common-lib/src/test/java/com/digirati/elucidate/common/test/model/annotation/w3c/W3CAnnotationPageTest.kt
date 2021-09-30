package com.digirati.elucidate.common.test.model.annotation.w3c

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationPageTest
import org.junit.Test

class W3CAnnotationPageTest : AbstractAnnotationPageTest<W3CAnnotationPage?>() {
    @Test
    fun testW3CAnnotationPage() {
        val w3cAnnotationPage = W3CAnnotationPage()
        testAbstractAnnotationPage(w3cAnnotationPage)
    }
}