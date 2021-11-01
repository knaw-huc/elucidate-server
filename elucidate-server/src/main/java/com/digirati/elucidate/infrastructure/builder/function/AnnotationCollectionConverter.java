package com.digirati.elucidate.infrastructure.builder.function;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;

@FunctionalInterface
public interface AnnotationCollectionConverter<C extends AbstractAnnotationCollection> {

    @NotNull
    C convertToAnnotationCollection();
}
