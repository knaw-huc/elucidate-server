package com.digirati.elucidate.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.batch.AbstractBatchOperation;
import com.digirati.elucidate.service.batch.AnnotationBatchDeleteService;
import com.digirati.elucidate.service.batch.AnnotationBatchUpdateService;

public abstract class AbstractAnnotationBatchController<B extends AbstractBatchOperation> {

    public static final String REQUEST_PATH_UPDATE = "/services/batch/update";
    public static final String REQUEST_PATH_DELETE = "/services/batch/delete";

    private final AnnotationBatchUpdateService<B> annotationBatchUpdateService;
    private final AnnotationBatchDeleteService<B> annotationBatchDeleteService;

    protected AbstractAnnotationBatchController(AnnotationBatchUpdateService<B> annotationBatchUpdateService, AnnotationBatchDeleteService<B> annotationBatchDeleteService) {
        this.annotationBatchUpdateService = annotationBatchUpdateService;
        this.annotationBatchDeleteService = annotationBatchDeleteService;
    }

    @NotNull
    @RequestMapping(value = REQUEST_PATH_UPDATE, method = RequestMethod.POST)
    public ResponseEntity<B> postUpdate(@RequestBody B batchOperation) {

        ServiceResponse<B> serviceResponse = annotationBatchUpdateService.processBatchUpdate(batchOperation);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        batchOperation = serviceResponse.getObj();
        return ResponseEntity.status(HttpStatus.OK).body(batchOperation);
    }

    @NotNull
    @RequestMapping(value = REQUEST_PATH_DELETE, method = RequestMethod.POST)
    public ResponseEntity<B> postDelete(@RequestBody B batchOperation) {

        ServiceResponse<B> serviceResponse = annotationBatchDeleteService.processBatchDelete(batchOperation);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        batchOperation = serviceResponse.getObj();
        return ResponseEntity.ok().body(batchOperation);
    }
}
