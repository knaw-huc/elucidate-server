package com.digirati.elucidate.web.converter.oa.annotationcontainer.annotationpage;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotationPage;

@Component
public class TurtleOAAnnotationPageMessageConverter extends AbstractOAAnnotationPageMessageConverter {

    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOAAnnotationPageMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(OAAnnotationPage oaAnnotationPage, MediaType contentType) {
        Map<String, Object> jsonMap = oaAnnotationPage.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @Override
    protected OAAnnotationPage getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAAnnotationPage.class));
    }
}
