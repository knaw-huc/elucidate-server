package com.digirati.elucidate.repository;

import java.util.Date;
import java.util.List;

import com.digirati.elucidate.model.annotation.temporal.AnnotationTemporal;

public interface AnnotationTemporalStoreRepository {

    AnnotationTemporal createAnnotationTemporal(Integer annotationPk, Integer bodyPk, Integer targetPk, String type, Date value, String temporalJson);

    List<AnnotationTemporal> deleteAnnotationTemporals(Integer annotationPk, Integer bodyPk, Integer targetPk);
}
