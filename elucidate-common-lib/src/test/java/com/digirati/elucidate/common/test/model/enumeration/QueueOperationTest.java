package com.digirati.elucidate.common.test.model.enumeration;

import org.junit.Test;

import com.digirati.elucidate.common.model.enumeration.QueueOperation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class QueueOperationTest {

    @Test
    public void testQueueOperationCreate() {
        QueueOperation queueOperation = QueueOperation.CREATE;
        assertNotNull(queueOperation);
        assertThat(queueOperation, is(equalTo(QueueOperation.CREATE)));
    }

    @Test
    public void testQueueOperationUpdate() {
        QueueOperation queueOperation = QueueOperation.UPDATE;
        assertNotNull(queueOperation);
        assertThat(queueOperation, is(equalTo(QueueOperation.UPDATE)));
    }

    @Test
    public void testQueueOperationDelete() {
        QueueOperation queueOperation = QueueOperation.DELETE;
        assertNotNull(queueOperation);
        assertThat(queueOperation, is(equalTo(QueueOperation.DELETE)));
    }
}
