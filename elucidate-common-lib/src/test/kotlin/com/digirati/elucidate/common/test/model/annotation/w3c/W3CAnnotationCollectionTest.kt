package com.digirati.elucidate.common.test.model.annotation.w3c

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationCollectionTest
import org.junit.Test

class W3CAnnotationCollectionTest : AbstractAnnotationCollectionTest<W3CAnnotationCollection>() {
    @Test
    fun testW3CAnnotationCollection() {
        val w3cAnnotationCollection = W3CAnnotationCollection()
        testAbstractAnnotationCollection(w3cAnnotationCollection)
    }
}