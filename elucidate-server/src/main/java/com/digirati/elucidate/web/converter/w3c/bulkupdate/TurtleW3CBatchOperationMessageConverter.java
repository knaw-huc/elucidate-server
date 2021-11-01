package com.digirati.elucidate.web.converter.w3c.bulkupdate;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.model.batch.W3CBatchOperation;

@Component
public class TurtleW3CBatchOperationMessageConverter extends AbstractW3CBatchOperationMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleW3CBatchOperationMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(W3CBatchOperation w3cBatchOperation, MediaType contentType) {
        Map<String, Object> jsonMap = w3cBatchOperation.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected W3CBatchOperation getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CBatchOperation.class));
    }
}
