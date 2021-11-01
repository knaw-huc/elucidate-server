package com.digirati.elucidate.service.search;

import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.enumeration.ClientPreference;

public interface AbstractAnnotationCollectionSearchService<C extends AbstractAnnotationCollection> {

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByBody(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByTarget(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByCreator(List<String> levels, String type, String value, boolean strict, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByGenerator(List<String> levels, String type, String value, boolean strict, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByTemporal(List<String> levels, List<String> types, Date since, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> searchAnnotationCollectionByOverlap(int lowerLimit, int upperLimit, ClientPreference clientPref);
}
