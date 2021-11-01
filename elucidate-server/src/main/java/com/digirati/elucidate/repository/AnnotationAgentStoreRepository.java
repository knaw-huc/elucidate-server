package com.digirati.elucidate.repository;

import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.model.annotation.agent.AnnotationAgent;

public interface AnnotationAgentStoreRepository {

    @Nullable
    AnnotationAgent createAnnotationCreator(Integer annotationPK, Integer bodyPK, Integer targetPK, String creatorIri, String creatorJson, String[] types, String[] typesJson, String[] names, String[] namesJson, String nickname, String[] emails, String[] emailsJson, String[] emailSha1s, String[] emailSha1sJson, String[] homepages, String[] homepagesJson);

    @Nullable
    AnnotationAgent deleteAnnotationCreators(Integer annotationPK, Integer bodyPK, Integer targetPK);

    @Nullable
    AnnotationAgent createAnnotationGenerator(Integer annotationPk, Integer bodyPk, Integer targetPk, String generatorIri, String generatorJson, String[] types, String[] typesJson, String[] names, String[] namesJson, String nickname, String[] emails, String[] emailsJson, String[] emailSha1s, String[] emailSha1sJson, String[] homepages, String[] homepagesJson);

    @Nullable
    AnnotationAgent deleteAnnotationGenerators(Integer annotationPK, Integer bodyPK, Integer targetPK);
}
