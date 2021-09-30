package com.digirati.elucidate.common.test.model.annotation.oa

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationPage
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationPageTest
import org.junit.Test

class OAAnnotationPageTest : AbstractAnnotationPageTest<OAAnnotationPage>() {
    @Test
    fun testOAAnnotationPage() {
        val oaAnnotationPage = OAAnnotationPage()
        testAbstractAnnotationPage(oaAnnotationPage)
    }
}