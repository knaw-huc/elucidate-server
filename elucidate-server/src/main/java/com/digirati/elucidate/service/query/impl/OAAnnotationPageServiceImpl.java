package com.digirati.elucidate.service.query.impl;

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
import com.digirati.elucidate.service.query.OAAnnotationPageService;

@Service(OAAnnotationPageServiceImpl.SERVICE_NAME)
public class OAAnnotationPageServiceImpl extends AbstractAnnotationPageServiceImpl<OAAnnotation, OAAnnotationPage> implements OAAnnotationPageService {

    public static final String SERVICE_NAME = "oaAnnotationPageServiceImpl";

    private final IRIBuilderService iriBuilderService;

    @Autowired
    public OAAnnotationPageServiceImpl(IRIBuilderService iriBuilderService, @Value("${annotation.page.size}") int pageSize) {
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
    protected String buildCollectionIri(String searchQuery) {
        return iriBuilderService.buildCollectionIri(AnnotationFormat.OA, searchQuery);

    }

    @Override
    protected String buildPageIri(String searchQuery, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildCollectionPageIri(AnnotationFormat.OA, searchQuery, page, embeddedDescriptions);
    }
}
