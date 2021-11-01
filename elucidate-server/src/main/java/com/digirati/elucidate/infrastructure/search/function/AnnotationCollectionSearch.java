package com.digirati.elucidate.infrastructure.search.function;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.enumeration.ClientPreference;

@FunctionalInterface
public interface AnnotationCollectionSearch<C extends AbstractAnnotationCollection> {

    @NotNull
    ServiceResponse<C> searchAnnotationCollection(ClientPreference clientPref);
}
