package com.digirati.elucidate.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.infrastructure.exception.InvalidAnnotationException;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.ValidationError;
import com.digirati.elucidate.model.enumeration.ClientPreference;
import com.digirati.elucidate.service.query.AbstractAnnotationCollectionService;
import com.digirati.elucidate.service.query.AbstractAnnotationService;

public abstract class AbstractAnnotationWriteController<A extends AbstractAnnotation, C extends AbstractAnnotationCollection> {

    private static final String VARIABLE_COLLECTION_ID = "collectionId";
    private static final String VARIABLE_ANNOTATION_ID = "annotationId";
    private static final String CREATE_REQUEST_PATH = "/{" + VARIABLE_COLLECTION_ID + "}/";
    private static final String UPDATE_REQUEST_PATH = "/{" + VARIABLE_COLLECTION_ID + "}/{" + VARIABLE_ANNOTATION_ID + "}";

    private final AbstractAnnotationService<A> annotationService;
    private final AbstractAnnotationCollectionService<A, C> annotationCollectionService;

    protected AbstractAnnotationWriteController(AbstractAnnotationService<A> annotationService, AbstractAnnotationCollectionService<A, C> annotationCollectionService) {
        this.annotationService = annotationService;
        this.annotationCollectionService = annotationCollectionService;
    }

    @RequestMapping(value = CREATE_REQUEST_PATH, method = RequestMethod.POST)
    public ResponseEntity<A> postCreate(@PathVariable(VARIABLE_COLLECTION_ID) String collectionId, @RequestBody A annotation, HttpServletRequest request, HttpServletResponse response) {

        ServiceResponse<C> annotationCollectionServiceResponse = annotationCollectionService.getAnnotationCollection(collectionId, Collections.emptyList(), ClientPreference.MINIMAL_CONTAINER);
        Status annotationCollectionStatus = annotationCollectionServiceResponse.getStatus();

        if (annotationCollectionStatus.equals(Status.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String annotationId = request.getHeader("Slug");

        ServiceResponse<A> annotationServiceResponse = annotationService.createAnnotation(collectionId, annotationId, annotation);
        Status annotationStatus = annotationServiceResponse.getStatus();

        if (annotationStatus.equals(Status.ALREADY_EXISTS)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        if (annotationStatus.equals(Status.NON_CONFORMANT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (annotationStatus.equals(Status.OK)) {
            annotation = annotationServiceResponse.getObj();
            response.setHeader(HttpHeaders.LOCATION, (String) annotation.getJsonMap().get(JSONLDConstants.ATTRIBUTE_ID));
            return ResponseEntity.status(HttpStatus.CREATED).body(annotation);
        }

        throw new IllegalArgumentException(String.format("Unexpected service response status [%s]", annotationStatus));
    }

    @RequestMapping(value = UPDATE_REQUEST_PATH, method = RequestMethod.PUT)
    public ResponseEntity<A> postUpdate(@PathVariable(VARIABLE_COLLECTION_ID) String collectionId, @PathVariable(VARIABLE_ANNOTATION_ID) String annotationId, @RequestBody A annotation, HttpServletRequest request) {

        String cacheKey = request.getHeader(HttpHeaders.IF_MATCH);

        ServiceResponse<A> serviceResponse = annotationService.updateAnnotation(collectionId, annotationId, annotation, cacheKey);
        Status status = serviceResponse.getStatus();

        if (status.equals(Status.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (status.equals(Status.DELETED)) {
            return ResponseEntity.status(HttpStatus.GONE).body(null);
        }

        if (status.equals(Status.CACHE_KEY_MISS)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }

        if (status.equals(Status.ILLEGAL_MODIFICATION)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        if (status.equals(Status.NON_CONFORMANT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (status.equals(Status.OK)) {
            return ResponseEntity.ok(serviceResponse.getObj());
        }

        throw new IllegalArgumentException(String.format("Unexpected service response status [%s]", status));
    }

    @RequestMapping(value = UPDATE_REQUEST_PATH, method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(VARIABLE_COLLECTION_ID) String collectionId, @PathVariable(VARIABLE_ANNOTATION_ID) String annotationId, HttpServletRequest request) {

        String cacheKey = request.getHeader(HttpHeaders.IF_MATCH);

        ServiceResponse<Void> serviceResponse = annotationService.deleteAnnotation(collectionId, annotationId, cacheKey);
        Status status = serviceResponse.getStatus();

        if (status.equals(Status.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (status.equals(Status.DELETED)) {
            return ResponseEntity.status(HttpStatus.GONE).body(null);
        }

        if (status.equals(Status.CACHE_KEY_MISS)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }

        if (status.equals(Status.OK)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        throw new IllegalArgumentException(String.format("Unexpected service response status [%s]", status));
    }

    @ExceptionHandler(InvalidAnnotationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(InvalidAnnotationException invalidAnnotationException) {
        ValidationError validationError = new ValidationError();
        validationError.setJsonError(invalidAnnotationException.getErrorJson());
        return validationError;
    }
}
