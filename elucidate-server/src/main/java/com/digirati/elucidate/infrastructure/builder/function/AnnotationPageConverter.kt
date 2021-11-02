package com.digirati.elucidate.infrastructure.builder.function;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;

@FunctionalInterface
public interface AnnotationPageConverter<P extends AbstractAnnotationPage> {

    @NotNull
    P convertToAnnotationPage(Map<String, Object> jsonMap);
}
