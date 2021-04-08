package com.digirati.elucidate.service.history;

import java.util.List;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.annotation.history.AbstractAnnotationHistory;

public interface AbstractAnnotationHistoryService<A extends AbstractAnnotation, H extends AbstractAnnotationHistory> {

    ServiceResponse<H> recordAnnotationHistory(A annotation);

    ServiceResponse<List<H>> deleteAnnotationHistory(A annotation);

    ServiceResponse<H> getVersionedAnnotation(String collectionId, String annotationId, int version);

    ServiceResponse<H> getLatestAnnotationVersion(String collectionId, String annotationId);

    ServiceResponse<H> getPenultimateAnnotationVersion(String collectionId, String annotationId);

    ServiceResponse<H> getNextAnnotationVersion(String collectionId, String annotationId, int version);

    ServiceResponse<H> getPreviousAnnotationVersion(String collectionId, String annotationId, int version);
}
