package com.digirati.elucidate.common.test.model.annotation

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Assert

open class AbstractAnnotationCollectionTest<T : AbstractAnnotationCollection?> : AbstractObjectTest<T>() {
    protected fun testAbstractAnnotationCollection(abstractAnnotationCollection: T) {
        Assert.assertNotNull(abstractAnnotationCollection)
        val cacheKey = generateRandomCacheKey()
        abstractAnnotationCollection!!.cacheKey = cacheKey
        assertThat(
            cacheKey, Matchers.`is`(
                equalTo(
                    abstractAnnotationCollection.cacheKey
                )
            )
        )
        val collectionId = generateRandomId()
        abstractAnnotationCollection.collectionId = collectionId
        assertThat(
            collectionId, Matchers.`is`(
                equalTo(
                    abstractAnnotationCollection.collectionId
                )
            )
        )
        testAbstractObject(abstractAnnotationCollection)
    }
}