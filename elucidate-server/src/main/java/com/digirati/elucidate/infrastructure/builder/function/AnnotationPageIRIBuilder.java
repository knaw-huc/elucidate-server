package com.digirati.elucidate.infrastructure.builder.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AnnotationPageIRIBuilder {

    @NotNull
    String buildAnnotationPageIri(int page, boolean embeddedDescriptions);
}
