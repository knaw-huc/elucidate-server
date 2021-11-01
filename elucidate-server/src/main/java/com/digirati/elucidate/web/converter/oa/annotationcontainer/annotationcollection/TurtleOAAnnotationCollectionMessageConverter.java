package com.digirati.elucidate.web.converter.oa.annotationcontainer.annotationcollection;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationCollection;

@Component
public class TurtleOAAnnotationCollectionMessageConverter extends AbstractOAAnnotationCollectionMessageConverter {

    @NotNull
    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOAAnnotationCollectionMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(@NotNull OAAnnotationCollection oaAnnotationCollection, MediaType contentType) {
        Map<String, Object> jsonMap = oaAnnotationCollection.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @NotNull
    @Override
    protected OAAnnotationCollection getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAAnnotationCollection.class));
    }
}
