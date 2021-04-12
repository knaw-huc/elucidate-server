package com.digirati.elucidate.web.converter.oa.bulkupdate;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.model.batch.OABatchOperation;

@Component
public class TurtleOABatchOperationMessageConverter extends AbstractOABatchOperationMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOABatchOperationMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OABatchOperation oaBatchOperation, MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = oaBatchOperation.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OABatchOperation getObjectRepresentation(String str, MediaType contentType) throws Exception {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OABatchOperation.class));
    }
}
