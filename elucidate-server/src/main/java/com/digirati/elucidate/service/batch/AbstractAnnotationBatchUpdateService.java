package com.digirati.elucidate.service.batch;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.batch.AbstractBatchOperation;

public interface AbstractAnnotationBatchUpdateService<B extends AbstractBatchOperation> {

    @NotNull
    ServiceResponse<B> processBatchUpdate(B batchOperation);
}
