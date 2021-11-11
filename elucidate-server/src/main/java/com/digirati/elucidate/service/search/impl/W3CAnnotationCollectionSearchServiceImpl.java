package com.digirati.elucidate.service.search.impl;

import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.enumeration.ClientPreference;
import com.digirati.elucidate.service.search.W3CAnnotationCollectionSearchService;
import com.digirati.elucidate.service.search.W3CAnnotationPageSearchService;
import com.digirati.elucidate.service.search.W3CAnnotationSearchService;

@Service(W3CAnnotationCollectionSearchServiceImpl.SERVICE_NAME)
public class W3CAnnotationCollectionSearchServiceImpl extends AbstractAnnotationCollectionSearchServiceImpl<W3CAnnotation, W3CAnnotationPage, W3CAnnotationCollection> implements W3CAnnotationCollectionSearchService {

    public static final String SERVICE_NAME = "w3cAnnotationCollectionSearchServiceImpl";

    private final IRIBuilderService iriBuilderService;
    private final W3CAnnotationPageSearchService w3cAnnotationPageSearchService;

    @Autowired
    public W3CAnnotationCollectionSearchServiceImpl(IRIBuilderService iriBuilderService, W3CAnnotationSearchService w3cAnnotationSearchService, W3CAnnotationPageSearchService w3cAnnotationPageSearchService, @Value("${annotation.page.size}") int pageSize) {
        super(w3cAnnotationSearchService, pageSize);
        this.iriBuilderService = iriBuilderService;
        this.w3cAnnotationPageSearchService = w3cAnnotationPageSearchService;
    }

    @Override
    protected W3CAnnotationCollection convertToAnnotationCollection(W3CAnnotationCollection w3cAnnotationCollection) {
        return w3cAnnotationCollection;
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildBodySearchFirstAnnotationPage(List<W3CAnnotation> w3cAnnotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByBody(w3cAnnotations, fields, value, strict, xywh, t, creatorIri, generatorIri, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildW3CCollectionBodySearchIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageBodySearchIri(fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildTargetSearchFirstAnnotationPage(List<W3CAnnotation> w3cAnnotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByTarget(w3cAnnotations, fields, value, strict, xywh, t, creatorIri, generatorIri, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildW3CCollectionTargetSearchIri(fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageTargetSearchIri(fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildCreatorSearchFirstAnnotationPage(List<W3CAnnotation> w3cAnnotations, List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByCreator(w3cAnnotations, levels, type, value, strict, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildW3CCollectionCreatorSearchIri(levels, type, value, strict);
    }

    @Override
    protected String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageCreatorSearchIri(levels, type, value, strict, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildGeneratorSearchFirstAnnotationPage(List<W3CAnnotation> w3cAnnotations, List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByGenerator(w3cAnnotations, levels, type, value, strict, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildW3CCollectionGeneratorSearchIri(levels, type, value, strict);
    }

    @Override
    protected String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageGeneratorSearchIri(levels, type, value, strict, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildTemporalSearchFirstAnnotationPage(List<W3CAnnotation> w3cAnnotations, List<String> levels, List<String> types, Date since, @NotNull ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByTemporal(w3cAnnotations, levels, types, since, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildTemporalSearchCollectionIri(List<String> levels, List<String> types, Date since) {
        return iriBuilderService.buildW3CCollectionTemporalSearchIri(levels, types, since);
    }

    @Override
    protected String buildTemporalSearchPageIri(List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageTemporalSearchIri(levels, types, since, page, embeddedDescriptions);
    }

    // search by range

    @Override
    protected String buildRangeSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildW3CCollectionRangeSearchIri(targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildRangeSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageRangeSearchIri(targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildRangeSearchFirstAnnotationPage(List<W3CAnnotation> annotations, String targetId, int rangeStart, int rangeEnd, ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByRange(annotations, targetId, rangeStart, rangeEnd, 0, useEmbeddedDescriptions(clientPref));
    }

    // search by overlap

    @Override
    protected String buildOverlapSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildW3CCollectionOverlapSearchIri(targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildOverlapSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildW3CPageOverlapSearchIri(targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<W3CAnnotationPage> buildOverlapSearchFirstAnnotationPage(List<W3CAnnotation> annotations, String targetId, int rangeStart, int rangeEnd, ClientPreference clientPref) {
        return w3cAnnotationPageSearchService.buildAnnotationPageByOverlap(annotations, targetId, rangeStart, rangeEnd, 0, useEmbeddedDescriptions(clientPref));
    }
}
