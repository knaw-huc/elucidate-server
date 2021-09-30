package com.digirati.elucidate.web.converter.oa.annotation;

import java.util.Map;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.service.history.OAAnnotationHistoryService;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TurtleOAAnnotationMessageConverter extends AbstractOAAnnotationMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    @Autowired
    public TurtleOAAnnotationMessageConverter(IRIBuilderService iriBuilderService, OAAnnotationHistoryService oaAnnotationHistoryService) {
        super(iriBuilderService, oaAnnotationHistoryService, APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OAAnnotation oaAnnotation, MediaType contentType) {
        Map<String, Object> jsonMap = oaAnnotation.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OAAnnotation getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAAnnotation.class));
    }
}
