package com.digirati.elucidate.service.query;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.model.ServiceResponse;

public interface AbstractAnnotationService<A extends AbstractAnnotation> {

    @Nullable
    ServiceResponse<A> getAnnotation(String collectionId, String annotationId);

    @NotNull
    ServiceResponse<List<A>> getAnnotations(String collectionId);

    @Nullable
    ServiceResponse<A> createAnnotation(String collectionId, String annotationId, A annotation);

    @Nullable
    ServiceResponse<A> updateAnnotation(String collectionId, String annotationId, A annotation, String cacheKey);

    @Nullable
    ServiceResponse<Void> deleteAnnotation(String collectionId, String annotationId, String cacheKey);
}
