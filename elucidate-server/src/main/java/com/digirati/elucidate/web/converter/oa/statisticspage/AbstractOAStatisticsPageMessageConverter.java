package com.digirati.elucidate.web.converter.oa.statisticspage;

import com.digirati.elucidate.model.statistics.OAStatisticsPage;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract class AbstractOAStatisticsPageMessageConverter extends AbstractMessageConverter<OAStatisticsPage> {

    protected AbstractOAStatisticsPageMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return OAStatisticsPage.class.equals(clazz);
    }

    @Override
    protected void decorateHeaders(OAStatisticsPage oaStatisticsPage, HttpOutputMessage outputMessage) {
        outputMessage.getHeaders().add(HttpHeaders.ALLOW, "GET,OPTIONS,HEAD");
        outputMessage.getHeaders().add(HttpHeaders.VARY, "Accept");
        outputMessage.getHeaders().add("Accept-Post", "application/ld+json; profile=\"http://www.w3.org/ns/anno.jsonld\", text/turtle");
    }
}
