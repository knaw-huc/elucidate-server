package com.digirati.elucidate.web.converter.oa.annotationcontainer.annotationcollection;

import java.util.Map;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationCollection;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TurtleOAAnnotationCollectionMessageConverter extends AbstractOAAnnotationCollectionMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOAAnnotationCollectionMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OAAnnotationCollection oaAnnotationCollection, MediaType contentType) {
        Map<String, Object> jsonMap = oaAnnotationCollection.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OAAnnotationCollection getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAAnnotationCollection.class));
    }
}
