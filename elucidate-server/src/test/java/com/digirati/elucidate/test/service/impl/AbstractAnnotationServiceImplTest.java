package com.digirati.elucidate.test.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.common.test.AbstractTest;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.repository.AnnotationStoreRepository;
import com.digirati.elucidate.service.query.AbstractAnnotationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractAnnotationServiceImplTest<A extends AbstractAnnotation, C extends AbstractAnnotationCollection> extends AbstractTest {

    private AnnotationStoreRepository annotationStoreRepository;
    private AbstractAnnotationService<A> annotationService;

    protected abstract AbstractAnnotationService<A> createAnnotationService(IRIBuilderService iriBuilderService, AnnotationStoreRepository annotationStoreRepository);

    protected abstract void validateConversionToAnnotation(W3CAnnotation w3cAnnotation, A targetAnnotation);

    protected abstract A generateAnnotationWithJsonMapOnly();

    @Before
    public void before() {
        IRIBuilderService iriBuilderService = mock(IRIBuilderService.class);
        this.annotationStoreRepository = mock(AnnotationStoreRepository.class);
        this.annotationService = createAnnotationService(iriBuilderService, annotationStoreRepository);
    }

    @Test
    public void testGetAnnotationOk() {

        W3CAnnotation w3cAnnotation = generateRandomW3CAnnotation();
        String collectionId = w3cAnnotation.getCollectionId();
        String annotationId = w3cAnnotation.getAnnotationId();

        when(annotationStoreRepository.getAnnotationByCollectionIdAndAnnotationId(collectionId, annotationId)).thenReturn(w3cAnnotation);

        ServiceResponse<A> serviceResponse = annotationService.getAnnotation(collectionId, annotationId);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(Status.OK)));

        validateConversionToAnnotation(w3cAnnotation, serviceResponse.getObj());
    }

    @Test
    public void testGetAnnotationNotFound() {

        W3CAnnotation w3cAnnotation = generateRandomW3CAnnotation();
        String collectionId = w3cAnnotation.getCollectionId();
        String annotationId = w3cAnnotation.getAnnotationId();

        when(annotationStoreRepository.getAnnotationByCollectionIdAndAnnotationId(collectionId, annotationId)).thenReturn(null);

        ServiceResponse<A> serviceResponse = annotationService.getAnnotation(collectionId, annotationId);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(Status.NOT_FOUND)));
        assertThat(serviceResponse.getObj(), is(nullValue()));
    }

    @Test
    public void testGetAnnotationDeleted() {

        W3CAnnotation w3cAnnotation = generateRandomW3CAnnotation();
        String collectionId = w3cAnnotation.getCollectionId();
        String annotationId = w3cAnnotation.getAnnotationId();

        when(annotationStoreRepository.countDeletedAnnotations(collectionId, annotationId)).thenReturn(1);
        when(annotationStoreRepository.getAnnotationByCollectionIdAndAnnotationId(collectionId, annotationId)).thenReturn(null);

        ServiceResponse<A> serviceResponse = annotationService.getAnnotation(collectionId, annotationId);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(Status.DELETED)));
        assertThat(serviceResponse.getObj(), is(nullValue()));
    }

    @Test
    public void testGetAnnotations() {

        String collectionId = generateRandomId();
        List<W3CAnnotation> w3cAnnotations = generateRandomW3CAnnotations(10);

        when(annotationStoreRepository.getAnnotationsByCollectionId(collectionId)).thenReturn(w3cAnnotations);

        ServiceResponse<List<A>> serviceResponse = annotationService.getAnnotations(collectionId);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(Status.OK)));

        List<A> targetAnnotations = serviceResponse.getObj();
        assertThat(targetAnnotations, is(not(nullValue())));
        assertThat(targetAnnotations.size(), is(equalTo(10)));

        for (int i = 0; i < 10; i++) {
            validateConversionToAnnotation(w3cAnnotations.get(i), targetAnnotations.get(i));
        }
    }

    @Test
    public void testCreateAnnotationWithoutAnnotationId() {

        String collectionId = generateRandomId();
        A annotation = generateAnnotationWithJsonMapOnly();

        W3CAnnotation w3cAnnotation = new W3CAnnotation();
        w3cAnnotation.setAnnotationId("test-annotation-id");
        w3cAnnotation.setCollectionId(collectionId);
        w3cAnnotation.setCreatedDateTime(new Date());
        w3cAnnotation.setDeleted(false);
        w3cAnnotation.setJsonMap(annotation.getJsonMap());

        when(annotationStoreRepository.createAnnotation(eq(collectionId), eq("test-annotation-id"), anyString(), ArgumentMatchers.any())).thenReturn(w3cAnnotation);

        ServiceResponse<A> serviceResponse = annotationService.createAnnotation(collectionId, null, annotation);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(Status.OK)));

        A targetAnnotation = serviceResponse.getObj();
        assertThat(targetAnnotation, is(not(nullValue())));
        assertThat(targetAnnotation.getCollectionId(), is(equalTo(collectionId)));
        assertThat(targetAnnotation.getAnnotationId(), is(not(nullValue())));
    }

    @Test
    public void testCreateAnnotationWithContextlessCustomFields() {

        String collectionId = generateRandomId();
        A annotation = generateAnnotationWithJsonMapOnly();

        W3CAnnotation w3cAnnotation = new W3CAnnotation();
        w3cAnnotation.setAnnotationId("test-annotation-id");
        w3cAnnotation.setCollectionId(collectionId);
        w3cAnnotation.setCreatedDateTime(new Date());
        w3cAnnotation.setDeleted(false);
        Map<String, Object> jsonMap = annotation.getJsonMap();
        jsonMap.put("custom_field","custom_value");
        w3cAnnotation.setJsonMap(jsonMap);

        when(annotationStoreRepository.createAnnotation(eq(collectionId), eq("test-annotation-id"), anyString(), ArgumentMatchers.any())).thenReturn(w3cAnnotation);

        ServiceResponse<A> serviceResponse = annotationService.createAnnotation(collectionId, null, annotation);
        assertThat(serviceResponse, is(not(nullValue())));
        assertThat(serviceResponse.getStatus(), is(equalTo(ServiceResponse.Status.OK)));

        A targetAnnotation = serviceResponse.getObj();
        assertThat(targetAnnotation, is(not(nullValue())));
        assertThat(targetAnnotation.getCollectionId(), is(equalTo(collectionId)));
        assertThat(targetAnnotation.getAnnotationId(), is(not(nullValue())));
        assertThat(targetAnnotation.getJsonMap(), hasKey("custom_field"));
        assertThat(targetAnnotation.getJsonMap().get("custom_field"), is("custom_value"));
    }


//    public void testUpdateAnnotation() {
//        // TODO
//    }
//
//    public void testDeleteAnnotation() {
//        // TODO
//    }
}
