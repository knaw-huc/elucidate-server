package com.digirati.elucidate.repository;

import java.util.List;

import com.digirati.elucidate.model.annotation.body.AnnotationBody;

public interface AnnotationBodyStoreRepository {

    AnnotationBody createAnnotationBody(int annotationPK, String bodyIri, String sourceIri, String bodyJson);

    List<AnnotationBody> deletedAnnotationBodies(int annotationPK);
}
