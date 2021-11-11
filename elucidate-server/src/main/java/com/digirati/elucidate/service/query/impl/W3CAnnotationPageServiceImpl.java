package com.digirati.elucidate.service.query.impl;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.AnnotationFormat;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.service.query.W3CAnnotationPageService;

@Service(W3CAnnotationPageServiceImpl.SERVICE_NAME)
public class W3CAnnotationPageServiceImpl extends AbstractAnnotationPageServiceImpl<W3CAnnotation, W3CAnnotationPage> implements W3CAnnotationPageService {

    public static final String SERVICE_NAME = "w3cAnnotationPageServiceImpl";

    private final IRIBuilderService iriBuilderService;

    public W3CAnnotationPageServiceImpl(IRIBuilderService iriBuilderService, @Value("${annotation.page.size}") int pageSize) {
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
    protected String buildCollectionIri(String searchQuery) {
        return iriBuilderService.buildCollectionIri(AnnotationFormat.W3C, searchQuery);
    }

    @Override
    protected String buildPageIri(String searchQuery, int page, boolean embeddedDescriptions) {
        return iriBuilderService.buildCollectionPageIri(AnnotationFormat.W3C, searchQuery, page, embeddedDescriptions);
    }
}
