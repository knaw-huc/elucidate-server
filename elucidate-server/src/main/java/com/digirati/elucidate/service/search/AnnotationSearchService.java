package com.digirati.elucidate.service.search;

import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.model.ServiceResponse;

public interface AnnotationSearchService<A extends AbstractAnnotation> {

    @Nullable
    ServiceResponse<List<A>> searchAnnotationsByBody(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    @Nullable
    ServiceResponse<List<A>> searchAnnotationsByTarget(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    @Nullable
    ServiceResponse<List<A>> searchAnnotationsByCreator(List<String> levels, String type, String value, boolean strict);

    @Nullable
    ServiceResponse<List<A>> searchAnnotationsByGenerator(List<String> levels, String type, String value, boolean strict);

    @Nullable
    ServiceResponse<List<A>> searchAnnotationsByTemporal(List<String> levels, List<String> types, Date since);

    @NotNull
    ServiceResponse<List<A>> searchAnnotationsByRange(String targetId, int rangeStart, int rangeEnd);

    @NotNull
    ServiceResponse<List<A>> searchAnnotationsByOverlap(String targetId, int rangeStart, int rangeEnd);
}
