package com.digirati.elucidate.service.history;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.annotation.history.AbstractAnnotationHistory;

public interface AnnotationHistoryService<A extends AbstractAnnotation, H extends AbstractAnnotationHistory> {

    @NotNull
    ServiceResponse<H> recordAnnotationHistory(A annotation);

    @Nullable
    ServiceResponse<List<H>> deleteAnnotationHistory(A annotation);

    @NotNull
    ServiceResponse<H> getVersionedAnnotation(String collectionId, String annotationId, int version);

    @NotNull
    ServiceResponse<H> getLatestAnnotationVersion(String collectionId, String annotationId);

    @NotNull
    ServiceResponse<H> getPenultimateAnnotationVersion(String collectionId, String annotationId);

    @NotNull
    ServiceResponse<H> getNextAnnotationVersion(String collectionId, String annotationId, int version);

    @NotNull
    ServiceResponse<H> getPreviousAnnotationVersion(String collectionId, String annotationId, int version);
}
