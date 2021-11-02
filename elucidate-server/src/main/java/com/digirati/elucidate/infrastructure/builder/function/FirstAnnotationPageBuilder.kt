package com.digirati.elucidate.infrastructure.builder.function;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;
import com.digirati.elucidate.model.ServiceResponse;

@FunctionalInterface
public interface FirstAnnotationPageBuilder<P extends AbstractAnnotationPage> {

    @NotNull
    ServiceResponse<P> buildFirstAnnotationPage();
}
