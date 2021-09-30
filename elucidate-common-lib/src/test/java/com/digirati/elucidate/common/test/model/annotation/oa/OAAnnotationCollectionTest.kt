package com.digirati.elucidate.common.test.model.annotation.oa

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationCollection
import com.digirati.elucidate.common.test.model.annotation.AbstractAnnotationCollectionTest
import org.junit.Test

class OAAnnotationCollectionTest : AbstractAnnotationCollectionTest<OAAnnotationCollection>() {
    @Test
    fun testOAAnnotationCollection() {
        val oaAnnotationCollection = OAAnnotationCollection()
        testAbstractAnnotationCollection(oaAnnotationCollection)
    }
}