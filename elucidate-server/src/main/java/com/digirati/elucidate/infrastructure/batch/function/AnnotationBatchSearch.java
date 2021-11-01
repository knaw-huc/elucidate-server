package com.digirati.elucidate.infrastructure.batch.function;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;

@FunctionalInterface
public interface AnnotationBatchSearch<A extends AbstractAnnotation> {

    @NotNull
    List<A> searchForAnnotations(List<String> searchFields, String searchValue);
}
