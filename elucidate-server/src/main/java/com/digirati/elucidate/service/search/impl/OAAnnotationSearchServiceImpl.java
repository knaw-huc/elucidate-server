package com.digirati.elucidate.service.search.impl;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.converter.W3CToOAAnnotationConverter;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetailsContext;
import com.digirati.elucidate.repository.AnnotationSearchRepository;
import com.digirati.elucidate.service.search.OAAnnotationSearchService;

@Service(OAAnnotationSearchServiceImpl.SERVICE_NAME)
public class OAAnnotationSearchServiceImpl extends AbstractAnnotationSearchServiceImpl<OAAnnotation> implements OAAnnotationSearchService {

    public static final String SERVICE_NAME = "oaAnnotationSearchServiceImpl";

    private final IRIBuilderService iriBuilderService;

    @Autowired
    public OAAnnotationSearchServiceImpl(UserSecurityDetailsContext securityContext, AnnotationSearchRepository annotationSearchRepository, IRIBuilderService iriBuilderService) {
        super(securityContext, annotationSearchRepository);
        this.iriBuilderService = iriBuilderService;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected OAAnnotation convertToAnnotation(@NotNull W3CAnnotation w3cAnnotation) {

        Map<String, Object> w3cAnnotationMap = w3cAnnotation.getJsonMap();
        JsonNode w3cAnnotationNode = new ObjectMapper().convertValue(w3cAnnotationMap, JsonNode.class);

        JsonNode oaAnnotationNode = W3CToOAAnnotationConverter.INSTANCE.convert(w3cAnnotationNode);
        Map<String, Object> oaAnnotationMap = new ObjectMapper().convertValue(oaAnnotationNode, Map.class);

        OAAnnotation oaAnnotation = new OAAnnotation();
        oaAnnotation.setPk(w3cAnnotation.getPk());
        oaAnnotation.setCacheKey(w3cAnnotation.getCacheKey());
        oaAnnotation.setCollectionId(w3cAnnotation.getCollectionId());
        oaAnnotation.setCreatedDateTime(w3cAnnotation.getCreatedDateTime());
        oaAnnotation.setDeleted(w3cAnnotation.isDeleted());
        oaAnnotation.setAnnotationId(w3cAnnotation.getAnnotationId());
        oaAnnotation.setJsonMap(oaAnnotationMap);
        oaAnnotation.setModifiedDateTime(w3cAnnotation.getModifiedDateTime());
        return oaAnnotation;
    }

    @Override
    protected String buildAnnotationIri(String collectionId, String annotationId) {
        return iriBuilderService.buildOAAnnotationIri(collectionId, annotationId);
    }
}
