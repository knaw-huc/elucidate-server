package com.digirati.elucidate.web.converter.w3c.annotation;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.service.history.W3CAnnotationHistoryService;

@Component
public class TurtleW3CAnnotationMessageConverter extends AbstractW3CAnnotationMessageConverter {

    @NotNull
    private final NQuadTripleCallback nQuadTripleCallback;

    @Autowired
    public TurtleW3CAnnotationMessageConverter(IRIBuilderService iriBuilderService, W3CAnnotationHistoryService w3cAnnotationHistoryService) {
        super(iriBuilderService, w3cAnnotationHistoryService, APPLICATION_TURTLE);
        this.nQuadTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(@NotNull W3CAnnotation w3cAnnotation, MediaType contentType) {
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, nQuadTripleCallback, jsonLdOptions).toString();
    }

    @NotNull
    @Override
    protected W3CAnnotation getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CAnnotation.class));
    }
}
