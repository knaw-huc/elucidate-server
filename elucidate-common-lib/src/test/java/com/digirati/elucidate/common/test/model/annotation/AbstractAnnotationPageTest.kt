package com.digirati.elucidate.common.test.model.annotation

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage
import org.junit.Assert.assertNotNull

open class AbstractAnnotationPageTest<T : AbstractAnnotationPage?> : AbstractObjectTest<T>() {
    protected fun testAbstractAnnotationPage(abstractAnnotationPage: T) {
        assertNotNull(abstractAnnotationPage)
        testAbstractObject(abstractAnnotationPage)
    }
}