package com.digirati.elucidate.service.query;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.enumeration.ClientPreference;

public interface AnnotationCollectionService<A extends AbstractAnnotation, C extends AbstractAnnotationCollection> {

    @Nullable
    ServiceResponse<C> getAnnotationCollection(String collectionId, List<A> annotations, ClientPreference clientPref);

    @Nullable
    ServiceResponse<C> createAnnotationCollection(String collectionId, C annotationCollection);
}
