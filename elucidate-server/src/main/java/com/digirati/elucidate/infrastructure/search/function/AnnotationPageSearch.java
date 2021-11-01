package com.digirati.elucidate.infrastructure.search.function;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;
import com.digirati.elucidate.model.ServiceResponse;

@FunctionalInterface
public interface AnnotationPageSearch<P extends AbstractAnnotationPage> {

    @NotNull
    ServiceResponse<P> searchAnnotationPage(boolean embeddedDescriptions);
}
