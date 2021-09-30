package com.digirati.elucidate.common.test.model.annotation

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertNotNull

abstract class AbstractAnnotationTest<T : AbstractAnnotation?> : AbstractObjectTest<T>() {
    protected fun testAbstractAnnotation(abstractAnnotation: T) {
        assertNotNull(abstractAnnotation)
        val cacheKey = generateRandomCacheKey()
        abstractAnnotation!!.cacheKey = cacheKey
        MatcherAssert.assertThat(
            cacheKey, Matchers.`is`(
                Matchers.equalTo(
                    abstractAnnotation.cacheKey
                )
            )
        )
        val collectionId = generateRandomId()
        abstractAnnotation.collectionId = collectionId
        MatcherAssert.assertThat(
            collectionId, Matchers.`is`(
                Matchers.equalTo(
                    abstractAnnotation.collectionId
                )
            )
        )
        val annotationId = generateRandomId()
        abstractAnnotation.annotationId = annotationId
        MatcherAssert.assertThat(
            annotationId, Matchers.`is`(
                Matchers.equalTo(
                    abstractAnnotation.annotationId
                )
            )
        )
        testAbstractObject(abstractAnnotation)
    }
}