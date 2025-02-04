package com.digirati.elucidate.service.search.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;
import com.digirati.elucidate.infrastructure.builder.AnnotationPageBuilder;
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionIRIBuilder;
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageIRIBuilder;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.service.search.AnnotationPageSearchService;

public abstract class AbstractAnnotationPageSearchServiceImpl<A extends AbstractAnnotation, P extends AbstractAnnotationPage> implements AnnotationPageSearchService<A, P> {

    protected final Logger LOGGER = Logger.getLogger(getClass());

    private final int pageSize;

    protected AbstractAnnotationPageSearchServiceImpl(int pageSize) {
        this.pageSize = pageSize;
    }

    @NotNull
    protected abstract P convertToAnnotationPage(Map<String, Object> jsonMap);

    @Override
    public ServiceResponse<P> buildAnnotationPageByBody(@NotNull List<A> annotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildBodySearchCollectionIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildBodySearchPageIri(fields, value, strict, xywh, t, creatorIri, generatorIri, _page, _embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder).buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    protected abstract String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions);

    @Override
    public ServiceResponse<P> buildAnnotationPageByTarget(@NotNull List<A> annotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildTargetSearchCollectionIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildTargetSearchPageIri(fields, value, strict, xywh, t, creatorIri, generatorIri, _page, _embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder).buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    protected abstract String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions);

    @Override
    public ServiceResponse<P> buildAnnotationPageByCreator(@NotNull List<A> annotations, List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildCreatorSearchCollectionIri(levels, type, value, strict);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildCreatorSearchPageIri(levels, type, value, strict, page, embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder).buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict);

    protected abstract String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions);

    @Override
    public ServiceResponse<P> buildAnnotationPageByGenerator(@NotNull List<A> annotations, List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildGeneratorSearchCollectionIri(levels, type, value, strict);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildGeneratorSearchPageIri(levels, type, value, strict, page, embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder).buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict);

    protected abstract String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions);

    @Override
    public ServiceResponse<P> buildAnnotationPageByTemporal(@NotNull List<A> annotations, List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildTemporalSearchCollectionIri(levels, types, since);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildTemporalSearchPageIri(levels, types, since, page, embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder).buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildTemporalSearchCollectionIri(List<String> levels, List<String> types, Date since);

    protected abstract String buildTemporalSearchPageIri(List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions);

    // range

    @Override
    public ServiceResponse<P> buildAnnotationPageByRange(@NotNull List<A> annotations, String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder =
                () -> buildRangeSearchCollectionIri(targetId, rangeStart, rangeEnd);
        AnnotationPageIRIBuilder annotationPageIriBuilder =
                (int _page, boolean _embeddedDescriptions) -> buildRangeSearchPageIri(targetId, rangeStart, rangeEnd, page, embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder)
                .buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildRangeSearchCollectionIri(String targetId, float rangeStart, float rangeEnd);

    protected abstract String buildRangeSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions);

    // overlap

    @Override
    public ServiceResponse<P> buildAnnotationPageByOverlap(@NotNull List<A> annotations, String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions) {

        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder =
                () -> buildOverlapSearchCollectionIri(targetId, rangeStart, rangeEnd);
        AnnotationPageIRIBuilder annotationPageIriBuilder =
                (int _page, boolean _embeddedDescriptions) -> buildOverlapSearchPageIri(targetId, rangeStart, rangeEnd, page, embeddedDescriptions);

        return new AnnotationPageBuilder<A, P>(this::convertToAnnotationPage, annotationCollectionIriBuilder, annotationPageIriBuilder)
                .buildAnnotationPage(annotations, page, embeddedDescriptions, pageSize);
    }

    protected abstract String buildOverlapSearchCollectionIri(String targetId, float rangeStart, float rangeEnd);

    protected abstract String buildOverlapSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions);

}
