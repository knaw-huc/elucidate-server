package com.digirati.elucidate.common.test.model.annotation;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;

import static org.junit.Assert.assertNotNull;

public class AbstractAnnotationPageTest<T extends AbstractAnnotationPage> extends AbstractObjectTest<T> {

    protected void testAbstractAnnotationPage(T abstractAnnotationPage) {
        assertNotNull(abstractAnnotationPage);
        testAbstractObject(abstractAnnotationPage);
    }
}
