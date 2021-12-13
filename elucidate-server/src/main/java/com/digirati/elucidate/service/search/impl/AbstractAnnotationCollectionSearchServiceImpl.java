package com.digirati.elucidate.service.search.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection;
import com.digirati.elucidate.infrastructure.builder.AnnotationCollectionBuilder;
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionConverter;
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionIRIBuilder;
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageIRIBuilder;
import com.digirati.elucidate.infrastructure.builder.function.FirstAnnotationPageBuilder;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.enumeration.ClientPreference;
import com.digirati.elucidate.service.search.AnnotationCollectionSearchService;
import com.digirati.elucidate.service.search.AnnotationSearchService;

public abstract class AbstractAnnotationCollectionSearchServiceImpl<A extends AbstractAnnotation, P extends AbstractAnnotationPage, C extends AbstractAnnotationCollection> implements AnnotationCollectionSearchService<C> {

    protected final Logger LOGGER = Logger.getLogger(getClass());

    private final AnnotationSearchService<A> annotationSearchService;
    private final int pageSize;

    protected AbstractAnnotationCollectionSearchServiceImpl(AnnotationSearchService<A> annotationSearchService, int pageSize) {
        this.annotationSearchService = annotationSearchService;
        this.pageSize = pageSize;
    }

    protected abstract C convertToAnnotationCollection(W3CAnnotationCollection w3cAnnotationCollection);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByBody(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByBody(fields, value, strict, xywh, t, creatorIri, generatorIri);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildBodySearchCollectionIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildBodySearchPageIri(fields, value, strict, xywh, t, creatorIri, generatorIri, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildBodySearchFirstAnnotationPage(annotations, fields, value, strict, xywh, t, creatorIri, generatorIri, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder).buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract ServiceResponse<P> buildBodySearchFirstAnnotationPage(List<A> annotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, ClientPreference clientPref);

    protected boolean useEmbeddedDescriptions(@NotNull ClientPreference clientPref) {
        return !clientPref.equals(ClientPreference.CONTAINED_IRIS);
    }

    protected abstract String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    protected abstract String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByTarget(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByTarget(fields, value, strict, xywh, t, creatorIri, generatorIri);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildTargetSearchCollectionIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildTargetSearchPageIri(fields, value, strict, xywh, t, creatorIri, generatorIri, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildTargetSearchFirstAnnotationPage(annotations, fields, value, strict, xywh, t, creatorIri, generatorIri, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder).buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract ServiceResponse<P> buildTargetSearchFirstAnnotationPage(List<A> annotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, ClientPreference clientPref);

    protected abstract String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri);

    protected abstract String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByCreator(List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByCreator(levels, type, value, strict);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildCreatorSearchCollectionIri(levels, type, value, strict);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildCreatorSearchPageIri(levels, type, value, strict, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildCreatorSearchFirstAnnotationPage(annotations, levels, type, value, strict, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder).buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract ServiceResponse<P> buildCreatorSearchFirstAnnotationPage(List<A> annotations, List<String> levels, String type, String value, boolean strict, ClientPreference clientPref);

    protected abstract String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict);

    protected abstract String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByGenerator(List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByGenerator(levels, type, value, strict);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildGeneratorSearchCollectionIri(levels, type, value, strict);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildGeneratorSearchPageIri(levels, type, value, strict, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildGeneratorSearchFirstAnnotationPage(annotations, levels, type, value, strict, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder).buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract ServiceResponse<P> buildGeneratorSearchFirstAnnotationPage(List<A> annotations, List<String> levels, String type, String value, boolean strict, ClientPreference clientPref);

    protected abstract String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict);

    protected abstract String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByTemporal(List<String> levels, List<String> types, Date since, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByTemporal(levels, types, since);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildTemporalSearchCollectionIri(levels, types, since);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildTemporalSearchPageIri(levels, types, since, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildTemporalSearchFirstAnnotationPage(annotations, levels, types, since, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder).buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract ServiceResponse<P> buildTemporalSearchFirstAnnotationPage(List<A> annotations, List<String> levels, List<String> types, Date since, ClientPreference clientPref);

    protected abstract String buildTemporalSearchCollectionIri(List<String> levels, List<String> types, Date since);

    protected abstract String buildTemporalSearchPageIri(List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByRange(String targetId, float rangeStart, float rangeEnd, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByRange(targetId, rangeStart, rangeEnd);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildRangeSearchCollectionIri(targetId, rangeStart, rangeEnd);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildRangeSearchPageIri(targetId, rangeStart, rangeEnd, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildRangeSearchFirstAnnotationPage(annotations, targetId, rangeStart, rangeEnd, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder)
                .buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract String buildRangeSearchCollectionIri(String targetId, float rangeStart, float rangeEnd);

    protected abstract String buildRangeSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions);

    protected abstract ServiceResponse<P> buildRangeSearchFirstAnnotationPage(List<A> annotations, String targetId, float rangeStart, float rangeEnd, ClientPreference clientPref);

    @Nullable
    @Override
    public ServiceResponse<C> searchAnnotationCollectionByOverlap(String targetId, float rangeStart, float rangeEnd, @NotNull ClientPreference clientPref) {

        W3CAnnotationCollection w3cAnnotationCollection = new W3CAnnotationCollection();
        w3cAnnotationCollection.setJsonMap(new HashMap<>());

        ServiceResponse<List<A>> serviceResponse = annotationSearchService.searchAnnotationsByOverlap(targetId, rangeStart, rangeEnd);
        Status status = serviceResponse.getStatus();

        if (!status.equals(Status.OK)) {
            LOGGER.warn(String.format("Got unexpected service response code [%s] (expected [%s]", status, Status.OK));
            return new ServiceResponse<>(status, null);
        }

        List<A> annotations = serviceResponse.getObj();

        AnnotationCollectionConverter<C> annotationCollectionConverter = () -> convertToAnnotationCollection(w3cAnnotationCollection);
        AnnotationCollectionIRIBuilder annotationCollectionIriBuilder = () -> buildOverlapSearchCollectionIri(targetId, rangeStart, rangeEnd);
        AnnotationPageIRIBuilder annotationPageIriBuilder = (int _page, boolean _embeddedDescriptions) -> buildOverlapSearchPageIri(targetId, rangeStart, rangeEnd, _page, _embeddedDescriptions);
        FirstAnnotationPageBuilder<P> firstAnnotationPageBuilder = () -> buildOverlapSearchFirstAnnotationPage(annotations, targetId, rangeStart, rangeEnd, clientPref);

        return new AnnotationCollectionBuilder<A, P, C>(annotationCollectionConverter, annotationCollectionIriBuilder, annotationPageIriBuilder, firstAnnotationPageBuilder)
                .buildAnnotationCollection(w3cAnnotationCollection, annotations, pageSize, clientPref);
    }

    protected abstract String buildOverlapSearchCollectionIri(String targetId, float rangeStart, float rangeEnd);

    protected abstract String buildOverlapSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions);

    protected abstract ServiceResponse<P> buildOverlapSearchFirstAnnotationPage(List<A> annotations, String targetId, float rangeStart, float rangeEnd, ClientPreference clientPref);

}
