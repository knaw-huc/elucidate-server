package com.digirati.elucidate.common.test.model.annotation;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractAnnotationTest<T extends AbstractAnnotation> extends AbstractObjectTest<T> {

    protected void testAbstractAnnotation(T abstractAnnotation) {

        assertNotNull(abstractAnnotation);

        String cacheKey = generateRandomCacheKey();
        abstractAnnotation.setCacheKey(cacheKey);
        assertThat(cacheKey, is(equalTo(abstractAnnotation.getCacheKey())));

        String collectionId = generateRandomId();
        abstractAnnotation.setCollectionId(collectionId);
        assertThat(collectionId, is(equalTo(abstractAnnotation.getCollectionId())));

        String annotationId = generateRandomId();
        abstractAnnotation.setAnnotationId(annotationId);
        assertThat(annotationId, is(equalTo(abstractAnnotation.getAnnotationId())));

        testAbstractObject(abstractAnnotation);
    }
}
