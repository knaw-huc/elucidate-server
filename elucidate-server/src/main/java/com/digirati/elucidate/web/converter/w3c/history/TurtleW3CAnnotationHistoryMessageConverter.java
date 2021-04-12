package com.digirati.elucidate.web.converter.w3c.history;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.annotation.history.W3CAnnotationHistory;
import com.digirati.elucidate.service.history.W3CAnnotationHistoryService;

@Component
public class TurtleW3CAnnotationHistoryMessageConverter extends AbstractW3CAnnotationHistoryMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    @Autowired
    public TurtleW3CAnnotationHistoryMessageConverter(IRIBuilderService iriBuilderService, W3CAnnotationHistoryService w3cAnnotationHistoryService) {
        super(iriBuilderService, w3cAnnotationHistoryService, APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(W3CAnnotationHistory w3CAnnotationHistory, MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = w3CAnnotationHistory.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected W3CAnnotationHistory getObjectRepresentation(String str, MediaType contentType) throws Exception {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CAnnotationHistory.class));
    }
}
