package com.digirati.elucidate.service.search.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.AnnotationFormat;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.service.search.W3CAnnotationPageSearchService;

@Service(W3CAnnotationPageSearchServiceImpl.SERVICE_NAME)
public class W3CAnnotationPageSearchServiceImpl extends AbstractAnnotationPageSearchServiceImpl<W3CAnnotation, W3CAnnotationPage> implements W3CAnnotationPageSearchService {

    public static final String SERVICE_NAME = "w3cAnnotationPageSearchServiceImpl";
    private static final AnnotationFormat FORMAT = AnnotationFormat.W3C;

    private final IRIBuilderService iriBuilderService;

    @Autowired
    public W3CAnnotationPageSearchServiceImpl(IRIBuilderService iriBuilderService, @Value("${annotation.page.size}") int pageSize) {
        super(pageSize);
        this.iriBuilderService = iriBuilderService;
    }

    @NotNull
    @Override
    protected W3CAnnotationPage convertToAnnotationPage(Map<String, Object> w3cAnnotationPageMap) {

        W3CAnnotationPage w3cAnnotationPage = new W3CAnnotationPage();
        w3cAnnotationPage.setJsonMap(w3cAnnotationPageMap);
        return w3cAnnotationPage;
    }

    @Override
    protected String buildBodySearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByBodyIri(FORMAT, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildBodySearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByBodyPageIri(FORMAT, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected String buildTargetSearchCollectionIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {
        return iriBuilderService.buildSearchByTargetIri(FORMAT, fields, value, strict, xywh, t, creatorIri, generatorIri);
    }

    @Override
    protected String buildTargetSearchPageIri(List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByTargetPageIri(FORMAT, fields, value, strict, xywh, t, creatorIri, generatorIri, page, embeddedDescriptions);
    }

    @Override
    protected String buildCreatorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByCreatorIri(FORMAT, levels, type, value, strict);
    }

    @Override
    protected String buildCreatorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByCreatorPageIri(FORMAT, levels, type, value, strict, page, embeddedDescriptions);
    }

    @Override
    protected String buildGeneratorSearchCollectionIri(List<String> levels, String type, String value, boolean strict) {
        return iriBuilderService.buildSearchByGeneratorIri(FORMAT, levels, type, value, strict);
    }

    @Override
    protected String buildGeneratorSearchPageIri(List<String> levels, String type, String value, boolean strict, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByGeneratorPageIri(FORMAT, levels, type, value, strict, page, embeddedDescriptions);
    }

    @Override
    protected String buildTemporalSearchCollectionIri(List<String> levels, List<String> types, Date since) {
        return iriBuilderService.buildSearchByTemporalIri(FORMAT, levels, types, since);
    }

    @Override
    protected String buildTemporalSearchPageIri(List<String> levels, List<String> types, Date since, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByTemporalPageIri(FORMAT, levels, types, since, page, embeddedDescriptions);
    }

    @Override
    protected String buildRangeSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildSearchByRangeIri(FORMAT, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildRangeSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByRangePageIri(FORMAT, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }

    @Override
    protected String buildOverlapSearchCollectionIri(String targetId, int rangeStart, int rangeEnd) {
        return iriBuilderService.buildSearchByOverlapIri(FORMAT, targetId, rangeStart, rangeEnd);
    }

    @Override
    protected String buildOverlapSearchPageIri(String targetId, int rangeStart, int rangeEnd, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildSearchByOverlapPageIri(FORMAT, targetId, rangeStart, rangeEnd, page, embeddedDescriptions);
    }
}
