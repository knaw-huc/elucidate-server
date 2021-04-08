package com.digirati.elucidate.common.test.model.annotation;

import com.digirati.elucidate.common.model.annotation.AbstractObject;
import com.digirati.elucidate.common.test.AbstractTest;

import java.util.Date;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractObjectTest<T extends AbstractObject> extends AbstractTest {

    protected void testAbstractObject(T abstractObject) {

        assertNotNull(abstractObject);

        Date createdDateTime = generateRandomDate();
        abstractObject.setCreatedDateTime(createdDateTime);
        assertThat(createdDateTime, is(equalTo(abstractObject.getCreatedDateTime())));

        boolean deleted = generateRandomBoolean();
        abstractObject.setDeleted(deleted);
        assertThat(deleted, is(equalTo(abstractObject.isDeleted())));

        Map<String, Object> jsonMap = generateRandomJsonMap();
        abstractObject.setJsonMap(jsonMap);
        assertThat(jsonMap, is(equalTo(abstractObject.getJsonMap())));

        Date modifiedDateTime = generateRandomDate();
        abstractObject.setModifiedDateTime(modifiedDateTime);
        assertThat(modifiedDateTime, is(equalTo(abstractObject.getModifiedDateTime())));
    }
}
