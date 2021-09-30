package com.digirati.elucidate.web.converter.oa.history;

import java.util.Map;

import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.annotation.history.OAAnnotationHistory;
import com.digirati.elucidate.service.history.OAAnnotationHistoryService;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TurtleOAAnnotationHistoryMessageConverter extends AbstractOAAnnotationHistoryMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    @Autowired
    public TurtleOAAnnotationHistoryMessageConverter(IRIBuilderService iriBuilderService, OAAnnotationHistoryService oaAnnotationHistoryService) {
        super(iriBuilderService, oaAnnotationHistoryService, APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OAAnnotationHistory oaAnnotationHistory, MediaType contentType) {
        Map<String, Object> jsonMap = oaAnnotationHistory.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OAAnnotationHistory getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAAnnotationHistory.class));
    }
}
