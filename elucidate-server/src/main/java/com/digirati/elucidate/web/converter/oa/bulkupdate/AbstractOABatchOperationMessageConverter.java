package com.digirati.elucidate.web.converter.oa.bulkupdate;

import com.digirati.elucidate.model.batch.OABatchOperation;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract class AbstractOABatchOperationMessageConverter extends AbstractMessageConverter<OABatchOperation> {

    protected AbstractOABatchOperationMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return OABatchOperation.class.equals(clazz);
    }

    @Override
    protected void decorateHeaders(OABatchOperation oaBatchOperation, HttpOutputMessage outputMessage) {
    }
}
