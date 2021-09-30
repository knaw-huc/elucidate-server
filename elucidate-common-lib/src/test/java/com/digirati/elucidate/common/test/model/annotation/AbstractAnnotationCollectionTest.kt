package com.digirati.elucidate.common.test.model.annotation;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class AbstractAnnotationCollectionTest<T extends AbstractAnnotationCollection> extends AbstractObjectTest<T> {

    protected void testAbstractAnnotationCollection(T abstractAnnotationCollection) {

        assertNotNull(abstractAnnotationCollection);

        String cacheKey = generateRandomCacheKey();
        abstractAnnotationCollection.setCacheKey(cacheKey);
        assertThat(cacheKey, is(equalTo(abstractAnnotationCollection.getCacheKey())));

        String collectionId = generateRandomId();
        abstractAnnotationCollection.setCollectionId(collectionId);
        assertThat(collectionId, is(equalTo(abstractAnnotationCollection.getCollectionId())));

        testAbstractObject(abstractAnnotationCollection);
    }
}
