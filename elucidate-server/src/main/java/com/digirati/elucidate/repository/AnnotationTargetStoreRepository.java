package com.digirati.elucidate.repository;

import java.util.List;

import com.digirati.elucidate.model.annotation.targets.AnnotationTarget;

public interface AnnotationTargetStoreRepository {

    AnnotationTarget createAnnotationTarget(int annotationPK, String targetIri, String sourceIri, String targetJson);

    List<AnnotationTarget> deleteAnnotationTargets(int annotationPK);
}
