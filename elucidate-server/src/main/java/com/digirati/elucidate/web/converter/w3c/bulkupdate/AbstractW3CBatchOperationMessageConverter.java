package com.digirati.elucidate.web.converter.w3c.bulkupdate;

import com.digirati.elucidate.model.batch.W3CBatchOperation;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract class AbstractW3CBatchOperationMessageConverter extends AbstractMessageConverter<W3CBatchOperation> {

    protected AbstractW3CBatchOperationMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return W3CBatchOperation.class.equals(clazz);
    }

    @Override
    protected void decorateHeaders(W3CBatchOperation w3cBatchOperation, HttpOutputMessage outputMessage) {
    }
}
