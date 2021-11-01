package com.digirati.elucidate.web.converter.w3c.annotationcontainer.annotationcollection;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection;

@Component
public class TurtleW3CAnnotationCollectionMessageConverter extends AbstractW3CAnnotationCollectionMessageConverter {

    private final NQuadTripleCallback nQuadTripleCallback;

    public TurtleW3CAnnotationCollectionMessageConverter() {
        super(APPLICATION_TURTLE);
        this.nQuadTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(W3CAnnotationCollection w3cAnnotationCollection, MediaType contentType) {
        Map<String, Object> jsonMap = w3cAnnotationCollection.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, nQuadTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected W3CAnnotationCollection getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CAnnotationCollection.class));
    }
}
