package com.digirati.elucidate.web.converter.w3c.annotationcontainer.annotationpage;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage;

@Component
public class TurtleW3CAnnotationPageMessageConverter extends AbstractW3CAnnotationPageMessageConverter {

    @NotNull
    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleW3CAnnotationPageMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(@NotNull W3CAnnotationPage w3cAnnotationPage, MediaType contentType) {
        Map<String, Object> jsonMap = w3cAnnotationPage.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @NotNull
    @Override
    protected W3CAnnotationPage getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CAnnotationPage.class));
    }
}
