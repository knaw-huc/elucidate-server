package com.digirati.elucidate.service.search.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.AnnotationFormat;
import com.digirati.elucidate.common.model.annotation.oa.OAAnnotation;
import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationCollection;
import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationPage;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.converter.W3CToOAAnnotationCollectionConverter;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.enumeration.ClientPreference;
import com.digirati.elucidate.service.search.OAAnnotationCollectionSearchService;
import com.digirati.elucidate.service.search.OAAnnotationPageSearchService;
import com.digirati.elucidate.service.search.OAAnnotationSearchService;

@Service(OAAnnotationCollectionSearchServiceImpl.SERVICE_NAME)
public class OAAnnotationCollectionSearchServiceImpl extends AbstractAnnotationCollectionSearchServiceImpl<OAAnnotation, OAAnnotationPage, OAAnnotationCollection> implements OAAnnotationCollectionSearchService {

    public static final String SERVICE_NAME = "oaAnnotationCollectionSearchServiceImpl";

    private final IRIBuilderService iriBuilderService;
    private final OAAnnotationPageSearchService oaAnnotationPageSearchService;

    @Autowired
    public OAAnnotationCollectionSearchServiceImpl(IRIBuilderService iriBuilderService, OAAnnotationSearchService oaAnnotationSearchService, OAAnnotationPageSearchService oaAnnotationPageSearchService, @Value("${annotation.page.size}") int pageSize) {
        super(oaAnnotationSearchService, pageSize);
        this.iriBuilderService = iriBuilderService;
        this.oaAnnotationPageSearchService = oaAnnotationPageSearchService;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected OAAnnotationCollection convertToAnnotationCollection(@NotNull W3CAnnotationCollection w3cAnnotationCollection) {

        Map<String, Object> w3cAnnotationCollectionMap = w3cAnnotationCollection.getJsonMap();
        JsonNode w3cAnnotationCollectionNode = new ObjectMapper().convertValue(w3cAnnotationCollectionMap, JsonNode.class);

        JsonNode oaAnnotationCollectionNode = W3CToOAAnnotationCollectionConverter.INSTANCE.convert(w3cAnnotationCollectionNode);
        Map<String, Object> oaAnnotationCollectionMap = new ObjectMapper().convertValue(oaAnnotationCollectionNode, Map.class);

        OAAnnotationCollection oaAnnotationCollection = new OAAnnotationCollection();
        oaAnnotationCollection.setPk(w3cAnnotationCollection.getPk());
        oaAnnotationCollection.setCacheKey(w3cAnnotationCollection.getCacheKey());
        oaAnnotationCollection.setCreatedDateTime(w3cAnnotationCollection.getCreatedDateTime());
        oaAnnotationCollection.setDeleted(w3cAnnotationCollection.isDeleted());
        oaAnnotationCollection.setCollectionId(w3cAnnotationCollection.getCollectionId());
        oaAnnotationCollection.setJsonMap(oaAnnotationCollectionMap);
        oaAnnotationCollection.setModifiedDateTime(oaAnnotationCollection.getModifiedDateTime());
        return oaAnnotationCollection;
    }

    // body

    @Override
    protected ServiceResponse<OAAnnotationPage> buildBodySearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByBody(oaAnnotations, fields, value, strict, xywh, t, creatorIri, generatorIri, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByBodyIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByBodyPageIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    // target

    @Override
    protected ServiceResponse<OAAnnotationPage> buildTargetSearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByTarget(oaAnnotations, fields, value, strict, xywh, t, creatorIri, generatorIri, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByTargetIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByTargetPageIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    // creator

    @Override
    protected ServiceResponse<OAAnnotationPage> buildCreatorSearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByCreator(oaAnnotations, levels, type, value, strict, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByCreatorIri(AnnotationFormat.OA, levels, type, value, strict);
    }

    @Override
    protected String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByCreatorPageIri(AnnotationFormat.OA, levels, type, value, strict, page, embeddedDescriptions);
    }

    // generator

    @Override
    protected ServiceResponse<OAAnnotationPage> buildGeneratorSearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, List<String> levels, String type, String value, boolean strict, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByGenerator(oaAnnotations, levels, type, value, strict, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByGeneratorIri(AnnotationFormat.OA, levels, type, value, strict);
    }

    @Override
    protected String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByGeneratorPageIri(AnnotationFormat.OA, levels, type, value, strict, page, embeddedDescriptions);
    }

    // temporal

    @Override
    protected ServiceResponse<OAAnnotationPage> buildTemporalSearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, List<String> levels, List<String> types, Date since, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByTemporal(oaAnnotations, levels, types, since, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildTemporalSearchCollectionIri(List<String> levels, List<String> types, Date since) {
        return iriBuilderService.buildSearchByTemporalIri(AnnotationFormat.OA, levels, types, since);
    }

    @Override
    protected String buildTemporalSearchPageIri(List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByTemporalPageIri(AnnotationFormat.OA, levels, types, since, page, embeddedDescriptions);
    }

    // range

    @Override
    protected String buildRangeSearchCollectionIri(String targetId, float rangeStart, float rangeEnd) {
        return iriBuilderService.buildSearchByRangeIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildRangeSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByRangePageIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }

    @Override
    protected ServiceResponse<OAAnnotationPage> buildRangeSearchFirstAnnotationPage(List<OAAnnotation> annotations, String targetId, float rangeStart, float rangeEnd, ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByRange(annotations, targetId, rangeStart, rangeEnd, 0, useEmbeddedDescriptions(clientPref));
    }

    // overlap

    @Override
    protected ServiceResponse<OAAnnotationPage> buildOverlapSearchFirstAnnotationPage(List<OAAnnotation> oaAnnotations, String targetId, float rangeStart, float rangeEnd, @NotNull ClientPreference clientPref) {
        return oaAnnotationPageSearchService.buildAnnotationPageByOverlap(oaAnnotations, targetId, rangeStart, rangeEnd, 0, useEmbeddedDescriptions(clientPref));
    }

    @Override
    protected String buildOverlapSearchCollectionIri(String targetId, float rangeStart, float rangeEnd) {
        return iriBuilderService.buildSearchByOverlapIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildOverlapSearchPageIri(String targetId, float rangeStart, float rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByOverlapPageIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }
}
