package com.digirati.elucidate.web.converter.oa.bulkupdate;

import java.util.Map;

import com.digirati.elucidate.model.batch.OABatchOperation;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TurtleOABatchOperationMessageConverter extends AbstractOABatchOperationMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOABatchOperationMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OABatchOperation oaBatchOperation, MediaType contentType) {
        Map<String, Object> jsonMap = oaBatchOperation.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OABatchOperation getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OABatchOperation.class));
    }
}
