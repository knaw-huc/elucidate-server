package com.digirati.elucidate.infrastructure.builder.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AnnotationCollectionIRIBuilder {

    @NotNull
    String buildAnnotationCollectionIri();
}
