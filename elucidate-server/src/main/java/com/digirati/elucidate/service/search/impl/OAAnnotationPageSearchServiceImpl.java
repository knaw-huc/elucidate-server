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
import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationPage;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.converter.OAToW3CAnnotationPageConverter;
import com.digirati.elucidate.service.search.OAAnnotationPageSearchService;

@Service(OAAnnotationPageSearchServiceImpl.SERVICE_NAME)
public class OAAnnotationPageSearchServiceImpl extends AbstractAnnotationPageSearchServiceImpl<OAAnnotation, OAAnnotationPage> implements OAAnnotationPageSearchService {

    public static final String SERVICE_NAME = "oaAnnotationPageSearchServiceImpl";

    private final IRIBuilderService iriBuilderService;

    @Autowired
    public OAAnnotationPageSearchServiceImpl(IRIBuilderService iriBuilderService, @Value("${annotation.page.size}") int pageSize) {
        super(pageSize);
        this.iriBuilderService = iriBuilderService;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected OAAnnotationPage convertToAnnotationPage(Map<String, Object> w3cAnnotationPageMap) {

        JsonNode w3cAnnotationPageNode = new ObjectMapper().convertValue(w3cAnnotationPageMap, JsonNode.class);

        JsonNode oaAnnotationPageNode = OAToW3CAnnotationPageConverter.INSTANCE.convert(w3cAnnotationPageNode);
        Map<String, Object> oaAnnotationPageMap = new ObjectMapper().convertValue(oaAnnotationPageNode, Map.class);

        OAAnnotationPage oaAnnotationPage = new OAAnnotationPage();
        oaAnnotationPage.setJsonMap(oaAnnotationPageMap);
        return oaAnnotationPage;
    }

    @Override
    protected String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByBodyIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByBodyPageIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByTargetIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByTargetPageIri(AnnotationFormat.OA, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByCreatorIri(AnnotationFormat.OA, levels, type, value, strict);
    }

    @Override
    protected String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByCreatorPageIri(AnnotationFormat.OA, levels, type, value, strict, page, embeddedDescriptions);
    }

    @Override
    protected String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByGeneratorIri(AnnotationFormat.OA, levels, type, value, strict);
    }

    @Override
    protected String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByGeneratorPageIri(AnnotationFormat.OA, levels, type, value, strict, page, embeddedDescriptions);
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
    protected String buildRangeSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildSearchByRangeIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildRangeSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByRangePageIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }

    // overlap

    @Override
    protected String buildOverlapSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildSearchByOverlapIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildOverlapSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByOverlapPageIri(AnnotationFormat.OA, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }
}
