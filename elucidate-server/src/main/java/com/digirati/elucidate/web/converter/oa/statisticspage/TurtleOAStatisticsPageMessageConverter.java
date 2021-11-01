package com.digirati.elucidate.web.converter.oa.statisticspage;

import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.impl.NQuadTripleCallback;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.model.statistics.OAStatisticsPage;

@Component
public class TurtleOAStatisticsPageMessageConverter extends AbstractOAStatisticsPageMessageConverter {

    @NotNull
    private final NQuadTripleCallback turtleTripleCallback;

    public TurtleOAStatisticsPageMessageConverter() {
        super(APPLICATION_TURTLE);
        this.turtleTripleCallback = new NQuadTripleCallback();
    }

    @Override
    protected String getStringRepresentation(@NotNull OAStatisticsPage oaStatisticsPage, MediaType contentType) {
        Map<String, Object> jsonMap = oaStatisticsPage.getJsonMap();
        return JsonLdProcessor.toRDF(jsonMap, turtleTripleCallback, jsonLdOptions).toString();
    }

    @NotNull
    @Override
    protected OAStatisticsPage getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, OAStatisticsPage.class));
    }
}
